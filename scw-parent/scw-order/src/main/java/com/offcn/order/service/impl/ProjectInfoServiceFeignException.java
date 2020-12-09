package com.offcn.order.service.impl;

import com.offcn.dycommon.response.AppResponse;
import com.offcn.order.service.ProjectInfoServiceFeign;
import com.offcn.order.vo.resp.TReturn;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Auther: lhq
 * @Date: 2020/12/3 10:18
 * @Description:
 */
@Component
public class ProjectInfoServiceFeignException implements ProjectInfoServiceFeign {
    @Override
    public AppResponse<List<TReturn>> getReturnList(Integer projectId) {
        AppResponse response = AppResponse.fail(null);
        response.setMsg("调用查询回报增量列表服务发送异常，触发熔断");
        return response;
    }
}
