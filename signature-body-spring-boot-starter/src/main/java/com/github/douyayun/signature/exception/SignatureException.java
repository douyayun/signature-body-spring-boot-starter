package com.github.douyayun.signature.exception;

/**
 * 签名异常
 *
 * @author houp
 * @version 1.0.0
 * @date 2023/3/17 20:16
 */
public class SignatureException extends RuntimeException {

    /**
     * 全局错误码
     *
     * @see GlobalErrorCodeConstants
     */
    private Integer code = 1000;
    /**
     * 错误提示
     */
    private String message = "签名错误";

    /**
     * 空构造方法，避免反序列化问题
     */
    public SignatureException() {
    }

    public SignatureException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public SignatureException setCode(Integer code) {
        this.code = code;
        return this;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public SignatureException setMessage(String message) {
        this.message = message;
        return this;
    }

}
