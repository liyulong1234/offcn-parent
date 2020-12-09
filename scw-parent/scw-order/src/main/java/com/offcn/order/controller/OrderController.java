package com.offcn.order.controller;

import com.offcn.dycommon.response.AppResponse;
import com.offcn.order.pojo.TOrder;
import com.offcn.order.service.OrderService;
import com.offcn.order.vo.req.OrderInfoSubmitVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: lhq
 * @Date: 2020/12/3 10:44
 * @Description:
 */
@RestController
@RequestMapping("/order")
@Api(tags = "项目模块")    //在类上加说明
public class OrderController {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private OrderService orderService;


    @ApiOperation("保存订单")
    @PostMapping("/saveOrder")
    public AppResponse<TOrder> saveOrder(@RequestBody OrderInfoSubmitVo orderInfoSubmitVo) {
        //判断用户是否登录成功
        String memberId = redisTemplate.opsForValue().get(orderInfoSubmitVo.getAccessToken());
        if (StringUtils.isEmpty(memberId)) {
            AppResponse fail = AppResponse.fail(null);
            fail.setMsg("登录失败，无此权限操作");
            return fail;
        }
        //执行保存订单
        try {
            TOrder tOrder = orderService.saveOrder(orderInfoSubmitVo);
            return AppResponse.ok(tOrder);
        } catch (Exception e) {
            e.printStackTrace();
            return AppResponse.fail(null);
        }

    }
}
