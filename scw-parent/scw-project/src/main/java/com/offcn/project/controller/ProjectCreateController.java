package com.offcn.project.controller;

import com.alibaba.fastjson.JSON;
import com.offcn.dycommon.ProjectStatusEnume;
import com.offcn.dycommon.response.AppResponse;
import com.offcn.project.contants.ProjectContant;
import com.offcn.project.pojo.TReturn;
import com.offcn.project.service.ProjectCreateService;
import com.offcn.project.vo.req.ProjectBaseInfoVo;
import com.offcn.project.vo.req.ProjectRedisStorageVo;
import com.offcn.project.vo.req.ProjectReturnVo;
import com.offcn.vo.BaseVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: lhq
 * @Date: 2020/12/2 10:04
 * @Description:
 */
@RestController
@RequestMapping("/project")
@Api(tags = "发布、创建项目")    //在类上加说明
public class ProjectCreateController {


    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ProjectCreateService projectCreateService;

    @ApiOperation("项目发起第一步：发布项目，同意协议")
    @PostMapping("/init")
    public AppResponse<String> init(BaseVo baseVo) {
        //1.通过登录令牌获得用户ID
        String memberId = redisTemplate.opsForValue().get(baseVo.getAccessToken());
        if (StringUtils.isEmpty(memberId)) {
            return AppResponse.fail("还未登录，无此操作权限");
        }
        //2.执行创建项目方法
        String token = projectCreateService.initCreateProject(Integer.parseInt(memberId));
        //3.返回项目令牌
        return AppResponse.ok(token);

    }

    @ApiOperation("项目发起第二步：收集项目基本信息")
    @PostMapping("/saveBaseInfo")
    public AppResponse<String> saveBaseInfo(@RequestBody ProjectBaseInfoVo projectBaseInfoVo) {
        //1.通过项目token在缓存中得到临时对象
        String projectContext = redisTemplate.opsForValue().get(ProjectContant.TEMP_PROJECT_PREFIX + projectBaseInfoVo.getProjectToken());
        ProjectRedisStorageVo projectRedisStorageVo = JSON.parseObject(projectContext, ProjectRedisStorageVo.class);
        //2.复制属性 projectBaseInfoVo --》 redisStorageVo
        BeanUtils.copyProperties(projectBaseInfoVo, projectRedisStorageVo);
        //3.重新将临时对象放回到缓存中
        String jsonStr = JSON.toJSONString(projectRedisStorageVo);
        redisTemplate.opsForValue().set(ProjectContant.TEMP_PROJECT_PREFIX + projectBaseInfoVo.getProjectToken(), jsonStr);
        return AppResponse.ok("保存项目基本信息成功");
    }

    @ApiOperation("项目发起第三步：收集增量回报基本信息")
    @PostMapping("/saveReturnInfo")
    public AppResponse saveReturnInfo(@RequestBody List<ProjectReturnVo> projectReturnVoList) {
        //1.通过项目token在缓存中得到临时项目
        String projectToken = projectReturnVoList.get(0).getProjectToken();
        String projectContext = redisTemplate.opsForValue().get(ProjectContant.TEMP_PROJECT_PREFIX + projectToken);
        ProjectRedisStorageVo projectRedisStorageVo = JSON.parseObject(projectContext, ProjectRedisStorageVo.class);
        //2.遍历projectReturnVoList 复制属性  ProjectReturnVo--> TReturn
        List<TReturn> returnList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(projectReturnVoList)) {
            for (ProjectReturnVo projectReturnVo : projectReturnVoList) {
                TReturn tReturn = new TReturn();
                BeanUtils.copyProperties(projectReturnVo, tReturn);
                returnList.add(tReturn);
            }
        }
        projectRedisStorageVo.setProjectReturns(returnList);

        //3.重新将临时对象放回到缓存中
        String jsonStr = JSON.toJSONString(projectRedisStorageVo);
        redisTemplate.opsForValue().set(ProjectContant.TEMP_PROJECT_PREFIX + projectToken, jsonStr);
        return AppResponse.ok("收集回报增量信息成功");

    }

    @ApiOperation("项目发起第四步：发布项目")
    @ApiImplicitParams({@ApiImplicitParam(name = "accessToken", value = "登录令牌", required = true), @ApiImplicitParam(name = "projectToken", value = "项目令牌", required = true), @ApiImplicitParam(name = "auth", value = "提交状态", required = true)})
    @PostMapping("/submit")
    public AppResponse<String> submit(String accessToken,String projectToken,String auth){
       String memberId =  redisTemplate.opsForValue().get(accessToken);
       if(StringUtils.isEmpty(memberId)){
           return AppResponse.fail("还未登录，无此操作权限");
       }
       String projectContext = redisTemplate.opsForValue().get(ProjectContant.TEMP_PROJECT_PREFIX+projectToken);
       ProjectRedisStorageVo projectRedisStorageVo = JSON.parseObject(projectContext,ProjectRedisStorageVo.class);

       if(StringUtils.isNotEmpty(auth)){
           //提交审核
           if(auth.equals("1")){
               ProjectStatusEnume submitAuth = ProjectStatusEnume.SUBMIT_AUTH;
               projectCreateService.saveProjectInfo(submitAuth,projectRedisStorageVo);
               return AppResponse.ok("保存成功");
           }
           //暂存草稿
           else if(auth.equals("0")){
               ProjectStatusEnume draftAuth = ProjectStatusEnume.DRAFT;
               projectCreateService.saveProjectInfo(draftAuth,projectRedisStorageVo);
               return AppResponse.ok("保存成功");
           }
           else{
               AppResponse response = AppResponse.fail(null);
               response.setMsg("不支持此操作");
               return response;
           }
       }else{
           return AppResponse.fail("保存失败");
       }
    }
}
