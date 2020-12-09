package com.offcn.webui.service;

import com.offcn.dycommon.response.AppResponse;
import com.offcn.webui.config.FeignConfig;
import com.offcn.webui.service.impl.UserServiceFeignException;
import com.offcn.webui.vo.resp.UserAddressVo;
import com.offcn.webui.vo.resp.UserRespVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Auther: lhq
 * @Date: 2020/12/7 10:07
 * @Description:
 */
@FeignClient(value = "SCWUSER", configuration = FeignConfig.class, fallback = UserServiceFeignException.class)
public interface UserServiceFeign {

    @GetMapping("/user/login")    //注意：Feign远程服务调用接口时，如果不使用占位符的传值方式，并且参数超过两个以上，需要加@RequestParam
    public AppResponse<UserRespVo> login(@RequestParam(name = "loginAcct") String loginAcct, @RequestParam(name = "password") String password);

    @GetMapping("/user/findMemberById/{id}")
    public AppResponse<UserRespVo> findMemberById(@PathVariable(name = "id") Integer id);

    @GetMapping("/user/findAddressList")
    public AppResponse<List<UserAddressVo>> findAddressList(@RequestParam(name="accessToken") String accessToken);
}
