package com.kep.portal.config.exception;


/**
 * DataNotFoundException
 * Data Exception 처리 
 * @생성일자	   / 생성자	  / 생성내용
 * 2023.10.04  / YO       / DataNotFoundException 데이터가 존재하지 않을 시 Exception 처리
 */
public class DataNotFoundException extends RuntimeException {
    
    public DataNotFoundException() {
        super();
    }

    public DataNotFoundException(String message) {
        super(message);
    }

    public DataNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
