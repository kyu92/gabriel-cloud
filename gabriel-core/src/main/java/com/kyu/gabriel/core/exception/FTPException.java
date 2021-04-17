package com.kyu.gabriel.core.exception;

public class FTPException extends RuntimeException{

    private static final long serialVersionUID = 8256298440320778633L;

    public FTPException(String msg){
        super(msg);
    }
}
