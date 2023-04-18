package io.github.douyayun.signature.storage.impl;

import io.github.douyayun.signature.properties.SignatureProperties;
import io.github.douyayun.signature.storage.NonceConfigStorage;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * TODO
 *
 * @author houp
 * @since 1.0.0
 */
@Slf4j
public class DefaultNonceConfigStorageImpl implements NonceConfigStorage, Serializable {

    private SignatureProperties signatureProperties;

    public DefaultNonceConfigStorageImpl(SignatureProperties signatureProperties) {
        log.info("DefaultNonceConfigStorageImpl...");
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
