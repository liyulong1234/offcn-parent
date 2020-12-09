package com.offcn.user.controller;

import com.offcn.dycommon.response.AppResponse;
import com.offcn.user.component.SmsTemplate;
import com.offcn.user.pojo.TMember;
import com.offcn.user.service.UserService;
import com.offcn.user.vo.resp.UserRespVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: lhq
 * @Date: 2020/11/30 15:02
 * @Description:
 */
@RestController
@RequestMapping("/user")
@Api(tags = "用户登录")    //在类上加说明
public class UserLoginController {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private SmsTemplate smsTemplate;

    @Autowired
    private UserService userService;

    @ApiOperation("获取短信验证码")
    @ApiImplicitParams({@ApiImplicitParam(name = "phone", value = "手机号", required = true)})
    @GetMapping("/sendCode")
    public AppResponse<String> sendCode(String phone) {
        //1.生成随机验证码
        String code = UUID.randomUUID().toString().substring(0, 4);
        //2.验证码暂存到缓存中   5分钟后超时，自动清空缓存
        redisTemplate.opsForValue().set(phone, code, 60 * 5, TimeUnit.SECONDS);
        //3.发送短信验证码
        String smsCode = smsTemplate.sendSms(phone, code);
        if (StringUtils.isEmpty(smsCode)) {
            return AppResponse.fail("获取验证码失败");
        } else {
            return AppResponse.ok(smsCode);
        }

    }


    @ApiOperation("用户登录")
    @ApiImplicitParams({@ApiImplicitParam(name = "loginAcct", value = "手机号", required = true),@ApiImplicitParam(name = "password", value = "密码", required = true)})
    @GetMapping("/login")
    public AppResponse<UserRespVo> login(String loginAcct, String password) {
        //1.判断用户登录是否成功
        TMember member = userService.login(loginAcct, password);
        if (null != member) {
            //2.随意生成一个访问令牌
            String accessToken = UUID.randomUUID().toString().replace("-", "");
            //3.将令牌和用户信息存入缓存，时限2小时
            redisTemplate.opsForValue().set(accessToken, member.getId() + "", 2, TimeUnit.HOURS);
            //4.复制属性
            UserRespVo respVo = new UserRespVo();
            BeanUtils.copyProperties(member, respVo);
            respVo.setAccessToken(accessToken);
            //5.将用户信息返回
            return AppResponse.ok(respVo);
        } else {
            AppResponse response = AppResponse.fail(null);
            response.setMsg("登录失败，请重新登录");
            return response;
        }

    }


}
