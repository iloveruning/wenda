package com.cll.wenda.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author chenliangliang
 * @date  2017/10/26
 */
@Data
@NoArgsConstructor
public class Question implements Serializable{

    private Integer id;

    @NotBlank(message = "标题不能为空")
    private String title;

    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date pubtime;

    @NotBlank(message = "提问内容不能为空")
    private String content;

    private String pic;

    private Integer ans;

    private Integer att;

    private String username;


    private Integer puber;

    private Integer readSize;

    private Integer isMyAtt;

    private Answer hotAnswer;

    private List<Answer> answers;

    /**
     * 是否匿名
     */
    private Integer isanony=0;

    /**
     * 话题
     */
    private String topic;


}
