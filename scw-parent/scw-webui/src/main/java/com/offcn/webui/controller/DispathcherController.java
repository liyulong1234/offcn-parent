package com.offcn.webui.controller;

import com.alibaba.fastjson.JSON;
import com.offcn.dycommon.response.AppResponse;
import com.offcn.webui.service.ProjectServiceFeign;
import com.offcn.webui.service.UserServiceFeign;
import com.offcn.webui.vo.resp.ProjectVo;
import com.offcn.webui.vo.resp.UserRespVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @Auther: lhq
 * @Date: 2020/12/7 09:28
 * @Description:
 */

@Controller
public class DispathcherController {

    @Autowired
    private UserServiceFeign userServiceFeign;

    @Autowired
    private ProjectServiceFeign projectServiceFeign;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @RequestMapping("/")
    public String index(Model model) {
        //1.读取缓存中的数据
        String projectStr = redisTemplate.opsForValue().get("projectStr");
        List<ProjectVo> projectVoList = JSON.parseArray(projectStr, ProjectVo.class);
        if (CollectionUtils.isEmpty(projectVoList)) {
            //2.判断缓存中的数据是否为空，如果为空，则远程服务调用，读取数据库中数据
            AppResponse<List<ProjectVo>> response = projectServiceFeign.findProjectAll();
            projectVoList = response.getData();
            //3.还需要将数据库中的数据重新存入到缓存中
            redisTemplate.opsForValue().set("projectStr", JSON.toJSONString(projectVoList));
        }else{
            System.out.println("从缓存读取数据");
        }
        model.addAttribute("projectList", projectVoList);
        return "index";
    }


    @RequestMapping("/doLogin")
    public String login(String loginacct, String password, HttpSession session) {
        //1.远程调用登录服务
        AppResponse<UserRespVo> response = userServiceFeign.login(loginacct, password);
        UserRespVo userRespVo = response.getData();
        //2.判断用户对象是否为空，如果为空，重新跳转到登录页面
        if (userRespVo == null) {
            return "redirect:/login.html";    //redirect: 资源重定向
        }
        //3.如果不为空，将用户信息设置到springSession中
        session.setAttribute("sessionMember", userRespVo);
        //4.读取session中的登录跳转路径
        String preUrl = (String) session.getAttribute("preUrl");
        if (StringUtils.isEmpty(preUrl)) {
            return "redirect:/";
        }
        return "redirect:" + preUrl;

    }
}
