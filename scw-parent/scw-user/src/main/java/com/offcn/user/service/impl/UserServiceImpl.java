package com.offcn.user.service.impl;

import com.offcn.user.enums.UserExceptionEnum;
import com.offcn.user.exception.UserException;
import com.offcn.user.mapper.TMemberAddressMapper;
import com.offcn.user.mapper.TMemberMapper;
import com.offcn.user.pojo.TMember;
import com.offcn.user.pojo.TMemberAddress;
import com.offcn.user.pojo.TMemberAddressExample;
import com.offcn.user.pojo.TMemberExample;
import com.offcn.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @Auther: lhq
 * @Date: 2020/12/1 09:24
 * @Description:
 */
@Service
public class UserServiceImpl implements UserService {


    @Autowired
    private TMemberMapper memberMapper;

    @Autowired
    private TMemberAddressMapper memberAddressMapper;

    /**
     * 用户注册
     *
     * @param tMember
     */
    @Override
    public void registUser(TMember tMember) {
        //1.判断帐号是否存在
        TMemberExample tMemberExample = new TMemberExample();
        TMemberExample.Criteria criteria = tMemberExample.createCriteria();
        criteria.andLoginacctEqualTo(tMember.getLoginacct());
        //select count(*) from t_member where loginacct=?
        long count = memberMapper.countByExample(tMemberExample);
        if (count > 0) {
            throw new UserException(UserExceptionEnum.LOGINACCT_EXIST);
        }
        //2.对密码加密
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = encoder.encode(tMember.getUserpswd());
        tMember.setUserpswd(password);
        //3.设置属性
        tMember.setUsername(tMember.getLoginacct());            //用户名
        tMember.setAuthstatus("0");                             //实名认证状态 0 - 未实名认证， 1 - 实名认证申请中， 2 - 已实名认证
        tMember.setUsertype("0");                               // 用户类型: 0 - 个人， 1 - 企业
        tMember.setAccttype("2");                               //账户类型: 0 - 企业， 1 - 个体， 2 - 个人， 3 - 政府
        //4.执行保存
        memberMapper.insertSelective(tMember);

    }

    /**
     * 用户登录
     *
     * @param loginAcct
     * @param password
     * @return
     */
    @Override
    public TMember login(String loginAcct, String password) {
        //1.根据用户名查询密码
        TMemberExample tMemberExample = new TMemberExample();
        TMemberExample.Criteria criteria = tMemberExample.createCriteria();
        criteria.andLoginacctEqualTo(loginAcct);
        List<TMember> memberList = memberMapper.selectByExample(tMemberExample);
        if (!CollectionUtils.isEmpty(memberList)) {
            TMember tMember = memberList.get(0);
            //2.密码做加密后的匹配
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            //参数一：明文密码  参数二：加密密码
            boolean match = encoder.matches(password, tMember.getUserpswd());
            //3.匹配成功返回用户信息
            return match ? tMember : null;
        }
        return null;
    }

    /**
     * 根据编号查询用户
     *
     * @param id
     * @return
     */
    @Override
    public TMember findMemberById(Integer id) {
        return memberMapper.selectByPrimaryKey(id);
    }

    /**
     * 根据会员编号查询地址列表
     *
     * @param memberId
     * @return
     */
    @Override
    public List<TMemberAddress> findAddressList(Integer memberId) {
        TMemberAddressExample tMemberAddressExample = new TMemberAddressExample();
        TMemberAddressExample.Criteria criteria = tMemberAddressExample.createCriteria();
        criteria.andMemberidEqualTo(memberId);
        return memberAddressMapper.selectByExample(tMemberAddressExample);
    }
}
