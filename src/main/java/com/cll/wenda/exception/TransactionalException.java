package com.cll.wenda.exception;

/**
 * @author chenliangliang
 * @date: 2017/10/27
 */
public class TransactionalException extends RuntimeException {

    public TransactionalException(String msg){
        super(msg);
    }
}
