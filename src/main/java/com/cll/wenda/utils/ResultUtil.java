package com.cll.wenda.utils;

import com.cll.wenda.model.Result;

/**
 * @author chenliangliang
 * @date: 2017/10/26
 */
public class ResultUtil {

    public static Result failResult(String msg){
        return new Result(false,msg,null);
    }

    public static Result successResult(String msg,Object date){
        return new Result(true,msg,date);
    }

    public static Result successResult(Object date){
        return new Result(true,null,date);
    }
}
