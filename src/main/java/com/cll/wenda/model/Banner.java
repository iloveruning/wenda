package com.cll.wenda.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * @author chenliangliang
 * @date: 2017/11/26
 */
@Data
@NoArgsConstructor
public class Banner implements Serializable {

   @JSONField(serialize = false)
    private Integer id;

    @NotBlank(message = "轮播图不能为空")
    private String img;

    @NotBlank(message = "链接不能为空")
    private String url;

    private String title;

}
