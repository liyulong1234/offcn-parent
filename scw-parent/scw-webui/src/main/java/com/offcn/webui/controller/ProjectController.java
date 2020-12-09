package com.offcn.webui.controller;

import com.offcn.dycommon.response.AppResponse;
import com.offcn.webui.service.ProjectServiceFeign;
import com.offcn.webui.service.UserServiceFeign;
import com.offcn.webui.vo.resp.ProjectDetailVo;
import com.offcn.webui.vo.resp.ReturnPayConfirmVo;
import com.offcn.webui.vo.resp.UserAddressVo;
import com.offcn.webui.vo.resp.UserRespVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Auther: lhq
 * @Date: 2020/12/7 11:16
 * @Description:
 */
@Controller
@RequestMapping("/project")
public class ProjectController {

    @Autowired
    private ProjectServiceFeign projectServiceFeign;

    @Autowired
    private UserServiceFeign userServiceFeign;

    @RequestMapping("/projectInfo")
    public String findProjectInfo(Integer id, HttpSession session, Model model) {
        //1.远程服务调用查询项目详情
        AppResponse<ProjectDetailVo> response = projectServiceFeign.findProjectInfo(id);
        ProjectDetailVo projectDetailVo = response.getData();

        //2.将项目详情设置到作用域  model  session
        model.addAttribute("DetailVo", projectDetailVo);
        session.setAttribute("DetailVo", projectDetailVo);
        return "project/project";
    }

    @RequestMapping("/confirm/returns/{projectId}/{returnId}")
    public String findReturnInfo(@PathVariable(name = "projectId") Integer projectid, @PathVariable("returnId") Integer returnId, HttpSession session, Model model) {
        //1.在session中得到项目详情
        ProjectDetailVo projectDetailVo = (ProjectDetailVo) session.getAttribute("DetailVo");
        //2.远程调用回报增量服务
        AppResponse<ReturnPayConfirmVo> response = projectServiceFeign.findReturnInfo(returnId);
        ReturnPayConfirmVo returnPayConfirmVo = response.getData();
        //3.根据项目发起方ID远程调用会员详情信息服务
        AppResponse<UserRespVo> response1 = userServiceFeign.findMemberById(projectDetailVo.getMemberid());
        UserRespVo userRespVo = response1.getData();

        returnPayConfirmVo.setProjectName(projectDetailVo.getName());    //项目名称
        returnPayConfirmVo.setMemberName(userRespVo.getRealname());         //发起人名称
        returnPayConfirmVo.setProjectId(projectDetailVo.getId());//项目ID

        //4.回报增量信息设置到作用域  session model
        session.setAttribute("returnConfirm", returnPayConfirmVo);
        model.addAttribute("returnConfirm", returnPayConfirmVo);
        return "project/pay-step-1";
    }


    @RequestMapping("/confirm/order/{num}")
    public String confirmOrder(@PathVariable(name = "num") Integer num, HttpSession session, Model model) {
        //1.判断用户是否登录，取得当前登录人的信息
        UserRespVo userRespVo = (UserRespVo) session.getAttribute("sessionMember");
        if (userRespVo == null) {
            //设置当前页码到session，保证重新登录后直接跳转到该页面
            session.setAttribute("preUrl", "/project/confirm/order/" + num);
            //重新跳转到登录页面
            return "redirect:/login.html";
        }
        //2.根据会员ID调用远程服务查询地址列表
        AppResponse<List<UserAddressVo>> response = userServiceFeign.findAddressList(userRespVo.getAccessToken());
        List<UserAddressVo> userAddressVoList = response.getData();
        //3.将地址列表存入作用域 model
        model.addAttribute("addresses", userAddressVoList);
        //4.从session中取得回报增量的详情
        ReturnPayConfirmVo returnPayConfirmVo = (ReturnPayConfirmVo) session.getAttribute("returnConfirm");
        //5.计算支付总金额  回报数量*回报单价+运费
        Integer totalPrice = returnPayConfirmVo.getSupportmoney() * num + returnPayConfirmVo.getFreight();
        returnPayConfirmVo.setNum(num);         //购买数量
        returnPayConfirmVo.setTotalPrice(new BigDecimal(totalPrice));
        //6.重新设置回报增量信息到作用域 model
        session.setAttribute("returnConfirmSession", returnPayConfirmVo);
        return "project/pay-step-2";
    }
}
