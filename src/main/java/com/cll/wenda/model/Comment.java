package com.cll.wenda.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author chenliangliang
 * @date: 2017/10/26
 */
@Data
@NoArgsConstructor
public class Comment {

    private Integer id;

    private  Integer puber;

    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date pubtime;

    @NotBlank(message = "评论内容不能为空")
    private String content;

    private String pic;

    private String dz;

    /**
     * 我是否点过赞
     */
    private Integer zg;

    /**
     * 回复数
     */
    private Integer reply;

    @NotNull(message = "要评论的回答ID不能为空")
    private Integer aid;

    private String author;


}
