package com.offcn.webui.service.impl;

import com.offcn.dycommon.response.AppResponse;
import com.offcn.webui.service.OrderServiceFeign;
import com.offcn.webui.vo.resp.OrderFormInfoSubmitVo;
import com.offcn.webui.vo.resp.TOrder;
import org.springframework.stereotype.Component;

/**
 * @Auther: lhq
 * @Date: 2020/12/7 15:20
 * @Description:
 */
@Component
public class OrderServiceFeignException implements OrderServiceFeign {

    @Override
    public AppResponse<TOrder> saveOrder(OrderFormInfoSubmitVo orderFormInfoSubmitVo) {
        AppResponse response = AppResponse.fail(null);
        response.setMsg("远程服务调用失败【保存订单】");
        return response;
    }
}
