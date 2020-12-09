package com.offcn.user.service;

import com.offcn.user.pojo.TMember;
import com.offcn.user.pojo.TMemberAddress;

import java.util.List;

/**
 * @Auther: lhq
 * @Date: 2020/12/1 09:22
 * @Description: 用户模块接口
 */
public interface UserService {

    /**
     * 用户注册
     *
     * @param tMember
     */
    public void registUser(TMember tMember);

    /**
     * 用户登录
     *
     * @param loginAcct
     * @param password
     * @return
     */
    public TMember login(String loginAcct, String password);

    /**
     * 根据编号查询用户
     * @param id
     * @return
     */
    public TMember  findMemberById(Integer id);


    /**
     * 根据会员编号查询地址列表
     * @param memberId
     * @return
     */
    public List<TMemberAddress> findAddressList(Integer memberId);
}
