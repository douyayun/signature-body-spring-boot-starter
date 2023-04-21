package io.github.douyayun.signature.storage;

import io.github.douyayun.signature.properties.SignatureProperties;

import java.util.List;

/**
 * TODO
 *
 * @author houp
 * @since 1.0.0
 **/
public interface SecretStorage {

    /**
     * 获取所有秘钥
     *
     * @return
     */
    List<SignatureProperties.Secret> getAllSecret();

    /**
     * 获取单个秘钥
     *
     * @param appId
     * @return
     */
    SignatureProperties.Secret getSecret(String appId);

    /**
     * 初始化秘钥
     *
     * @param secrets
     */
    void initSecret(List<SignatureProperties.Secret> secrets);

    /**
     * 追加秘钥
     *
     * @param secret
     */
    void appendSecret(SignatureProperties.Secret secret);

    /**
     * 移除单个秘钥
     *
     * @param appId
     */
    void removeSecret(String appId);

    /**
     * 移除所有秘钥
     */
    void removeAllSecret();
}
