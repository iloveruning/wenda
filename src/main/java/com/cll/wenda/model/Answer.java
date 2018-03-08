package com.cll.wenda.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author chenliangliang
 * @date: 2017/10/26
 */
@Data
@NoArgsConstructor
public class Answer implements Serializable{


    private Integer id;

    @NotBlank(message = "回答内容不能为空")
    private String content;

    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date pubtime;

    private String pic;

    @NotNull(message = " 要回答的问题ID不能为空")
    private Integer qid;

    private String username;

    /**
     * 收藏数
     */
    private Integer sc;


    private Integer puber;

    /**
     * 点赞数
     */
    private Integer dz;

    /**
     * 评论数
     */
    private Integer cmt;

    private String summary;

    private Comment hotComment;

    /**
     * 我是否点过赞
     */
    private Integer zg;

    /**
     * 是否是自己收藏
     */
    private Integer collect;


    /**
     * 是否匿名
     */
    private Integer isanony=0;


}
