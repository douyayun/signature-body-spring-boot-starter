package com.github.douyayun.signature.config.impl;

import com.github.douyayun.signature.config.NonceConfigStorage;
import com.github.douyayun.signature.properties.SignatureProperties;

import java.io.Serializable;

/**
 * TODO
 *
 * @author houp
 * @version v1.0.0
 * @date 2023/3/20 16:22
 */
public class RedisNonceConfigStorageImpl implements NonceConfigStorage, Serializable {

    private SignatureProperties signatureProperties;

    public RedisNonceConfigStorageImpl(SignatureProperties signatureProperties) {
        this.signatureProperties = signatureProperties;
    }

    /**
     * 获取票据  锁定当前appId的nonce. 防止重放攻击
     *
     * @param appId            appId值
     * @param nonce            nonce值
     * @param expiresInSeconds 过期时间，以秒为单位
     * @return 返回true表示可以使用；false表示已经存在，不能使用
     */
    @Override
    public boolean getTicket(String appId, String nonce, int expiresInSeconds) {
        return false;
    }
}
