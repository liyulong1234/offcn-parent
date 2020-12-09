package com.offcn.webui.vo.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Auther: lhq
 * @Date: 2020/12/7 14:23
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAddressVo implements Serializable {
    //地址id
    private Integer id;

    //会员地址
    private String address;

}
