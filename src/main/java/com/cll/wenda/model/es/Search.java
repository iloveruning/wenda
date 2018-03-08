package com.cll.wenda.model.es;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * @author chenliangliang
 * @date: 2017/11/22
 */
@Data
@NoArgsConstructor
public class Search implements Serializable {

    @NotBlank(message = "搜索关键字不能为空")
    private String keyword;

    private String type="question";

    private String sort="hot";

    private Integer pageNo=1;

    private Integer pageSize=4;

}
