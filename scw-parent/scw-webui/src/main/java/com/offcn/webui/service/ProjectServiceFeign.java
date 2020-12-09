package com.offcn.webui.service;

import com.offcn.dycommon.response.AppResponse;
import com.offcn.webui.config.FeignConfig;
import com.offcn.webui.service.impl.ProjectServiceFeignException;
import com.offcn.webui.vo.resp.ProjectDetailVo;
import com.offcn.webui.vo.resp.ProjectVo;
import com.offcn.webui.vo.resp.ReturnPayConfirmVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @Auther: lhq
 * @Date: 2020/12/7 10:41
 * @Description:
 */
@FeignClient(value = "SCWPROJECT",configuration = FeignConfig.class,fallback = ProjectServiceFeignException.class)
public interface ProjectServiceFeign {

    @GetMapping("/project/findProjectAll")
    public AppResponse<List<ProjectVo>> findProjectAll() ;

    @GetMapping("/project/findProjectInfo/{projectId}")
    public AppResponse<ProjectDetailVo> findProjectInfo(@PathVariable(name = "projectId") Integer projectId) ;

    @GetMapping("/project/returns/info/{returnId}")
    public AppResponse<ReturnPayConfirmVo> findReturnInfo(@PathVariable(name = "returnId") Integer returnId);
}
