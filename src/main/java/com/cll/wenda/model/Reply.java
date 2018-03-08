package com.cll.wenda.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author chenliangliang
 * @date: 2017/10/29
 */
@Data
@NoArgsConstructor
public class Reply implements Serializable {


    private Integer id;

    private Integer puber;

    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date pubtime;

    @NotBlank(message = "回复内容不能为空")
    private String content;

    private String pic;

    @NotNull(message = "要回复的评论ID不能为空")
    private Integer cid;

    private String author;

}
