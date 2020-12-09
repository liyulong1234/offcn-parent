package com.offcn.webui.vo.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Auther: lhq
 * @Date: 2020/12/7 11:13
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectReturnVo implements Serializable {
    //项目token
    private String projectToken;

    private Integer id;

    private Integer projectId;

    private Byte type;

    private Integer supportmoney;

    private String content;

    private Integer signalpurchase;

    private Integer purchase;

    private Integer freight;

    private Byte invoice;

    private Integer rtndate;

}
