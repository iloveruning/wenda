package com.cll.wenda.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author chenliangliang
 * @date: 2017/11/23
 */
@Data
@NoArgsConstructor
public class Keyword implements Serializable {

    private String keyword;

    /*private List<String> keywords;*/
}
