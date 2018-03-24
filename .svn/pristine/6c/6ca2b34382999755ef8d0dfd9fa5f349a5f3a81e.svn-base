package com.menyi.web.util;



/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: ÷‹–¬”Ó</p>
 *
 * @author not attributable
 * @version 1.0
 */
public class BusinessException extends RuntimeException implements ErrorCanst {
    public int code;
    public String key;
    public String backUrl;

    public BusinessException(int code, String key) {
        super(key);
        this.code = code;
        this.key = key;
    }
    public BusinessException(String key) {
        super(key);
        this.code = this.DEFAULT_FAILURE;
        this.key = key;
    }
    public BusinessException(String key,String backUrl) {
        super(key);
        this.code = this.DEFAULT_FAILURE;
        this.key = key;
        this.backUrl = backUrl;
    }


}
