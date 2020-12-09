package com.offcn.project.controller;

import com.offcn.dycommon.response.AppResponse;
import com.offcn.project.enums.ProjectImageTypeEnume;
import com.offcn.project.pojo.*;
import com.offcn.project.service.ProjectInfoService;
import com.offcn.project.vo.resp.ProjectDetailVo;
import com.offcn.project.vo.resp.ProjectVo;
import com.offcn.utils.OSSTemplate;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import io.swagger.annotations.ApiOperation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: lhq
 * @Date: 2020/12/1 14:33
 * @Description:
 */
@RestController
@RequestMapping("/project")
public class ProjectInfoController {

    @Autowired
    private OSSTemplate ossTemplate;

    @Autowired
    private ProjectInfoService projectInfoService;

    @ApiOperation("上传图片")
    @PostMapping("/uploadFile")
    public AppResponse uploadFile(@RequestParam(name = "file") MultipartFile[] multipartFiles) {

        Map map = new HashMap();
        List<String> urls = new ArrayList<>();
        if (null != multipartFiles && multipartFiles.length > 0) {
            //1.遍历上传文件流集合
            for (MultipartFile file : multipartFiles) {
                //2.得到上传文件的对象
                //3.得到上传文件的全名称
                String fileName = file.getOriginalFilename();
                //4.调用上传文件模板完成上传
                try {
                    String url = ossTemplate.upload(file.getInputStream(), fileName);
                    urls.add(url);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        map.put("urls", urls);
        return AppResponse.ok(map);
    }

    @ApiOperation("根据项目编号查询回报增量列表")
    @ApiImplicitParams({@ApiImplicitParam(name = "projectId", value = "项目编号", required = true)})
    @GetMapping("/details/returns/{projectId}")
    public AppResponse<List<TReturn>> getReturnList(@PathVariable(name = "projectId") Integer projectId) {
        List<TReturn> returnList = projectInfoService.getReturnList(projectId);
        return AppResponse.ok(returnList);
    }

    @ApiOperation("查询所有项目列表")
    @GetMapping("/findProjectAll")
    public AppResponse<List<ProjectVo>> findProjectAll() {
        //1.查询所有项目
        List<TProject> projectList = projectInfoService.findProjectAll();
        List<ProjectVo> projectVoList = new ArrayList<>();
        //2.遍历项目得到每个项目下的头部图片，并设置
        for (TProject tProject : projectList) {
            ProjectVo projectVo = new ProjectVo();
            BeanUtils.copyProperties(tProject, projectVo);    //pojo--->vo

            List<TProjectImages> projectImagesList = projectInfoService.findImageList(tProject.getId());
            for (TProjectImages tProjectImages : projectImagesList) {
                //判断是否是头部图片
                if (tProjectImages.getImgtype() == ProjectImageTypeEnume.HEADER.getCode().byteValue()) {
                    projectVo.setHeaderImage(tProjectImages.getImgurl());
                }
            }
            projectVoList.add(projectVo);
        }
        //3.返回项目信息
        return AppResponse.ok(projectVoList);
    }

    @ApiOperation("根据项目编号查询项目详情")
    @ApiImplicitParams({@ApiImplicitParam(name = "projectId", value = "项目编号", required = true)})
    @GetMapping("/findProjectInfo/{projectId}")
    public AppResponse<ProjectDetailVo> findProjectInfo(@PathVariable(name = "projectId") Integer projectId) {
        //1.根据项目编号查询项目信息
        TProject tProject = projectInfoService.findProjectInfo(projectId);
        ProjectDetailVo projectDetailVo = new ProjectDetailVo();
        BeanUtils.copyProperties(tProject, projectDetailVo);
        //2.根据项目编号查询图片列表，并设置
        List<TProjectImages> imagesList = projectInfoService.findImageList(projectId);
        List<String> urlList = new ArrayList<>();
        for (TProjectImages tProjectImages : imagesList) {
            //头部图片
            if (tProjectImages.getImgtype() == ProjectImageTypeEnume.HEADER.getCode().byteValue()) {
                projectDetailVo.setHeaderImage(tProjectImages.getImgurl());
            } else {   //详情图片
                urlList.add(tProjectImages.getImgurl());
               // projectDetailVo.getDetailsImage().add(tProjectImages.getImgurl());
                projectDetailVo.setDetailsImage(urlList);
            }
        }
        //3.根据项目编号查询回报增量列表，并设置
        List<TReturn> returnList = projectInfoService.getReturnList(projectId);
        projectDetailVo.setProjectReturns(returnList);
        return AppResponse.ok(projectDetailVo);
    }

    @ApiOperation("查询所有标签列表")
    @GetMapping("/findAllTag")
    public AppResponse<List<TTag>> findAllTag(){
        List<TTag> tagList = projectInfoService.findAllTag();
        return AppResponse.ok(tagList);
    }

    @ApiOperation("查询所有分类列表")
    @GetMapping("/findAllType")
    public AppResponse<List<TType>> findAllType(){
        List<TType> typeList = projectInfoService.findAllType();
        return AppResponse.ok(typeList);
    }
    @ApiOperation("查询回报增量详情")
    @ApiImplicitParams({@ApiImplicitParam(name = "returnId", value = "回报增量编号", required = true)})
    @GetMapping("/returns/info/{returnId}")
    public AppResponse<TReturn> findReturnInfo(@PathVariable(name = "returnId") Integer returnId){
        TReturn tReturn = projectInfoService.findReturnInfo(returnId);
        return AppResponse.ok(tReturn);
    }
}
