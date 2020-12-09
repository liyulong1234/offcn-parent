package com.offcn.user.controller;

import com.offcn.dycommon.response.AppResponse;
import com.offcn.user.pojo.TMember;
import com.offcn.user.pojo.TMemberAddress;
import com.offcn.user.service.UserService;
import com.offcn.user.vo.req.UserRegisVo;
import com.offcn.user.vo.resp.UserRespVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Auther: lhq
 * @Date: 2020/12/1 09:37
 * @Description:
 */
@RestController
@RequestMapping("/user")
@Api(tags = "用户注册")    //在类上加说明
public class UserController {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private UserService userService;

    @ApiOperation("注册用户")
    @PostMapping("/registerUser")
    public AppResponse registerUser(UserRegisVo userRegisVo) {
        //1.校验验证码是否匹配
        String code = redisTemplate.opsForValue().get(userRegisVo.getLoginacct());
        if (StringUtils.isNotEmpty(code)) {
            //equalsIgnoreCase  判断是否相等并忽略大小写
            if (code.equalsIgnoreCase(userRegisVo.getCode())) {
                //2.复制属性
                TMember tMember = new TMember();
                //参数一：数据源  参数二：目标数据
                BeanUtils.copyProperties(userRegisVo, tMember);
                //3.执行用户注册
                userService.registUser(tMember);
                //4.清空缓存，删除验证码
                redisTemplate.delete(userRegisVo.getLoginacct());

                return AppResponse.ok("注册成功");
            } else {
                return AppResponse.fail("验证码错误");
            }
        } else {
            return AppResponse.fail("验证码不存在");
        }


    }

    @ApiOperation("根据编号查询用户")
    @GetMapping("/findMemberById/{id}")
    public AppResponse<UserRespVo> findMemberById(@PathVariable(name = "id") Integer id) {
        TMember member = userService.findMemberById(id);
        UserRespVo userRespVo = new UserRespVo();
        BeanUtils.copyProperties(member, userRespVo);
        return AppResponse.ok(userRespVo);
    }


    @ApiOperation("根据会员编号查询地址列表")
    @ApiImplicitParams({@ApiImplicitParam(name = "accessToken", value = "登录令牌", required = true)})
    @GetMapping("/findAddressList")
    public AppResponse<List<TMemberAddress>> findAddressList(String accessToken) {
        String memberId = redisTemplate.opsForValue().get(accessToken);
        if (StringUtils.isEmpty(memberId)) {
            AppResponse fail = AppResponse.fail(null);
            fail.setMsg("登录失败，查无数据");
        }
        List<TMemberAddress> memberAddressList = userService.findAddressList(Integer.parseInt(memberId));
        return AppResponse.ok(memberAddressList);
    }
}
