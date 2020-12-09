package com.offcn.webui.service.impl;

import com.offcn.dycommon.response.AppResponse;
import com.offcn.webui.service.UserServiceFeign;
import com.offcn.webui.vo.resp.UserAddressVo;
import com.offcn.webui.vo.resp.UserRespVo;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Auther: lhq
 * @Date: 2020/12/7 10:12
 * @Description:
 */
@Component
public class UserServiceFeignException implements UserServiceFeign {
    @Override
    public AppResponse<UserRespVo> login(String loginAcct, String password) {
        AppResponse response = AppResponse.fail(null);
        response.setMsg("远程服务调用失败【登录服务】");
        return response;
    }

    @Override
    public AppResponse<UserRespVo> findMemberById(Integer id) {
        AppResponse response = AppResponse.fail(null);
        response.setMsg("远程服务调用失败【查询用户详情】");
        return response;
    }

    @Override
    public AppResponse<List<UserAddressVo>> findAddressList(String accessToken) {
        AppResponse response = AppResponse.fail(null);
        response.setMsg("远程服务调用失败【查询会员地址列表】");
        return response;
    }
}
