package com.offcn.webui.service;

import com.offcn.dycommon.response.AppResponse;
import com.offcn.webui.config.FeignConfig;
import com.offcn.webui.service.impl.OrderServiceFeignException;
import com.offcn.webui.vo.resp.OrderFormInfoSubmitVo;
import com.offcn.webui.vo.resp.TOrder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Auther: lhq
 * @Date: 2020/12/7 15:19
 * @Description:
 */
@FeignClient(value = "SCWORDER", configuration = FeignConfig.class, fallback = OrderServiceFeignException.class)
public interface OrderServiceFeign {

    @PostMapping("/order/saveOrder")
    public AppResponse<TOrder> saveOrder(@RequestBody OrderFormInfoSubmitVo orderFormInfoSubmitVo);
}
