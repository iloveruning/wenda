package com.cll.wenda.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.util.Date;

/**
 * @author chenliangliang
 * @date: 2017/11/19
 */
@Data
@NoArgsConstructor
public class User implements Serializable {

    private String uuid;

    @JsonIgnore
    private Integer id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @NotBlank(message = "微信名不能为空")
    private String wxname;

    @NotBlank(message = "微信头像不能为空")
    private String headimg;

    private Integer sex;

    private String username;

    @NotBlank(message = "code不能为空")
    private String code;


    private String openid;




}
