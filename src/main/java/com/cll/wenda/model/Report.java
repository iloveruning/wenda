package com.cll.wenda.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author chenliangliang
 * @date: 2017/10/27
 */
@Data
@NoArgsConstructor
public class Report implements Serializable {

    private Integer id;

    @NotBlank(message = "举报类型不能为空")
    private String type;

    @NotNull(message = "要举报的问题或回答的ID不能为空")
    private Integer typeId;

    @NotBlank(message = "举报的主题不能为空")
    private String radio;

    private Integer puber;

    private Date pubtime;

    @NotBlank(message = "举报的详细理由不能为空")
    @Length(max = 250,message = "不能超过250个字符")
    private String reason;
}
