package com.offcn.webui.controller;

import com.offcn.dycommon.response.AppResponse;
import com.offcn.webui.service.OrderServiceFeign;
import com.offcn.webui.vo.resp.OrderFormInfoSubmitVo;
import com.offcn.webui.vo.resp.ReturnPayConfirmVo;
import com.offcn.webui.vo.resp.TOrder;
import com.offcn.webui.vo.resp.UserRespVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

/**
 * @Auther: lhq
 * @Date: 2020/12/7 15:22
 * @Description:
 */
@Controller
@RequestMapping("/order")
@Slf4j   //private Logger logger = new Logger();
public class OrderController {


    @Autowired
    private OrderServiceFeign orderServiceFeign;

    @RequestMapping("/save")
    public String createOrder(OrderFormInfoSubmitVo orderFormInfoSubmitVo, HttpSession session) {
        //1.判断用户是否登录
        UserRespVo userRespVo = (UserRespVo) session.getAttribute("sessionMember");
        if (userRespVo == null) {
            return "redirect:/login.html";
        }
        //2.如果登录，则设置登录令牌
        orderFormInfoSubmitVo.setAccessToken(userRespVo.getAccessToken());   //登录令牌
        //3.从session中取得回报增量信息，并设置
        ReturnPayConfirmVo returnPayConfirmVo = (ReturnPayConfirmVo) session.getAttribute("returnConfirmSession");
        orderFormInfoSubmitVo.setProjectid(returnPayConfirmVo.getProjectId());
        orderFormInfoSubmitVo.setReturnid(returnPayConfirmVo.getId());
        orderFormInfoSubmitVo.setRtncount(returnPayConfirmVo.getNum());

        //4.远程服务调用保存订单
        AppResponse<TOrder> response =  orderServiceFeign.saveOrder(orderFormInfoSubmitVo);
        TOrder tOrder = response.getData();
        //5.返回订单信息
        log.info("orderNum:{}",tOrder.getOrdernum());    //订单编号
        log.info("money:{}",tOrder.getMoney());
        log.info("rtncount:{}",tOrder.getRtncount());
        return "/member/minecrowdfunding";

    }
}
