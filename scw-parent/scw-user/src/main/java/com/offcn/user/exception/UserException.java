package com.offcn.user.exception;

import com.offcn.user.enums.UserExceptionEnum;

/**
 * @Auther: lhq
 * @Date: 2020/12/1 09:18
 * @Description: 自定义异常
 */
public class UserException extends RuntimeException{

    public UserException(UserExceptionEnum userExceptionEnum){
        super(userExceptionEnum.getMsg());
    }
}
