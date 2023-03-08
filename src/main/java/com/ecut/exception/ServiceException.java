package com.ecut.exception;

/**
 *@Author: 周伟
 *@CreateTime: 2023-01-05  09:34
 *@Version: 1.0
 */
public class ServiceException extends RuntimeException{
    public String getCode() {
        return code;
    }

    private String code;


    public ServiceException() {
    }
    public ServiceException(String code,String message){
        super(message);
        this.code = code;
    }
    public ServiceException(String message) {
        super(message);
    }
}
