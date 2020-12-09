package com.offcn.project.service.impl;

import com.alibaba.fastjson.JSON;
import com.offcn.dycommon.ProjectStatusEnume;
import com.offcn.project.contants.ProjectContant;
import com.offcn.project.enums.ProjectImageTypeEnume;
import com.offcn.project.mapper.*;
import com.offcn.project.pojo.*;
import com.offcn.project.service.ProjectCreateService;
import com.offcn.project.vo.req.ProjectRedisStorageVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @Auther: lhq
 * @Date: 2020/12/2 09:42
 * @Description:
 */
@Service
public class ProjectCreateServiceImpl implements ProjectCreateService {

    @Autowired
    private StringRedisTemplate redisTemplate;


    @Autowired
    private TProjectMapper projectMapper;

    @Autowired
    private TProjectImagesMapper projectImagesMapper;

    @Autowired
    private TProjectTypeMapper projectTypeMapper;

    @Autowired
    private TProjectTagMapper projectTagMapper;

    @Autowired
    private TReturnMapper returnMapper;

    /**
     * 初始化创建项目
     *
     * @param memberId 用户ID
     * @return
     */
    @Override
    public String initCreateProject(Integer memberId) {
        //1.初始化临时对象
        ProjectRedisStorageVo projectRedisStorageVo = new ProjectRedisStorageVo();
        //2.设置用户ID到临时对象
        projectRedisStorageVo.setMemberid(memberId);
        //3.创建一个随机的项目令牌，并设置
        String token = UUID.randomUUID().toString().replace("-","");
        //4.将临时项目存入缓存中
        String jsonStr = JSON.toJSONString(projectRedisStorageVo);
        redisTemplate.opsForValue().set(ProjectContant.TEMP_PROJECT_PREFIX+token,jsonStr);
        return token;
    }

    /**
     * 保存项目信息
     *
     * @param auth
     * @param projectRedisStorageVo
     */
    @Override
    public void saveProjectInfo(ProjectStatusEnume auth, ProjectRedisStorageVo projectRedisStorageVo) {
        //1.保存项目信息
        /**
         * 保存项目的基本信息，获取到数据库的id
         * 页面考过来的：name,remark,money,day,memberid
         * 我们自己后来操作：status,deploydate,createdate
         * 自动化修改：supportmoney（已支持的金额），supporter（支持人数） ,completion（完成度）,follower（关注者人数）
         */
        TProject tProject = new TProject();
        BeanUtils.copyProperties(projectRedisStorageVo,tProject);
        tProject.setStatus(auth.getCode()+"");   //0 - 即将开始， 1 - 众筹中， 2 - 众筹成功， 3 - 众筹失败
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        tProject.setCreatedate(sdf.format(new Date()));
        tProject.setMoney(projectRedisStorageVo.getMoney().longValue());
        projectMapper.insertSelective(tProject);
        Integer projectId = tProject.getId();    //获得项目主键
        //2.保存图片信息
        //头部图片
        TProjectImages headerImage = new TProjectImages(null,projectId,projectRedisStorageVo.getHeaderImage(), ProjectImageTypeEnume.HEADER.getCode().byteValue());
        projectImagesMapper.insertSelective(headerImage);
        //详情图片
        if(!CollectionUtils.isEmpty(projectRedisStorageVo.getDetailsImage())){
            for(String detailsUrl:projectRedisStorageVo.getDetailsImage()){
                TProjectImages detailsImage  = new TProjectImages(null,projectId,detailsUrl,ProjectImageTypeEnume.DETAILS.getCode().byteValue());
                projectImagesMapper.insertSelective(detailsImage);
            }
        }
        //3.保存分类信息
        if(!CollectionUtils.isEmpty(projectRedisStorageVo.getTypeids())){
            for(Integer typeId:projectRedisStorageVo.getTypeids()){
                TProjectType projectType = new TProjectType(null,projectId,typeId);
                projectTypeMapper.insertSelective(projectType);
            }
        }
        //4.保存标签信息
        if(!CollectionUtils.isEmpty(projectRedisStorageVo.getTagids())){
            for(Integer tagId:projectRedisStorageVo.getTagids()){
                TProjectTag projectTag = new TProjectTag(null,projectId,tagId);
                projectTagMapper.insertSelective(projectTag);
            }
        }
        //5.保存回报增量信息
        List<TReturn> returnList = projectRedisStorageVo.getProjectReturns();
        for(TReturn tReturn:returnList){
            tReturn.setProjectid(projectId);
            returnMapper.insertSelective(tReturn);
        }
        //6.清空缓存数据
        redisTemplate.delete(ProjectContant.TEMP_PROJECT_PREFIX+projectRedisStorageVo.getProjectToken());
    }
}
