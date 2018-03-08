package com.cll.wenda.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author chenliangliang
 * @date: 2017/10/26
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result implements Serializable{

    private  boolean status;

    private String msg;

    private Object data;
}
