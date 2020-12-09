package com.offcn.webui.service.impl;

import com.offcn.dycommon.response.AppResponse;
import com.offcn.webui.service.ProjectServiceFeign;
import com.offcn.webui.vo.resp.ProjectDetailVo;
import com.offcn.webui.vo.resp.ProjectVo;
import com.offcn.webui.vo.resp.ReturnPayConfirmVo;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Auther: lhq
 * @Date: 2020/12/7 10:42
 * @Description:
 */
@Component
public class ProjectServiceFeignException implements ProjectServiceFeign {
    @Override
    public AppResponse<List<ProjectVo>> findProjectAll() {
        AppResponse response = AppResponse.fail(null);
        response.setMsg("远程服务调用失败【项目列表查询服务】");
        return response;
    }

    @Override
    public AppResponse<ProjectDetailVo> findProjectInfo(Integer projectId) {
        AppResponse response = AppResponse.fail(null);
        response.setMsg("远程服务调用失败【根据项目编号查询项目详情】");
        return response;
    }

    @Override
    public AppResponse<ReturnPayConfirmVo> findReturnInfo(Integer returnId) {
        AppResponse response = AppResponse.fail(null);
        response.setMsg("远程服务调用失败【查询回报增量详情服务】");
        return response;
    }
}
