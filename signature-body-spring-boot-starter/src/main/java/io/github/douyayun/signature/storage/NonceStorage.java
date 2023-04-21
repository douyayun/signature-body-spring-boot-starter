package io.github.douyayun.signature.storage;

/**
 * nonce唯一性验证配置存储
 *
 * @author houp
 * @since 1.0.0
 */
public interface NonceStorage {

    /**
     * 唯一请求  锁定当前appId的nonce. 防止重放攻击
     *
     * @param appId            appId值
     * @param nonce            nonce值
     * @param expiresInSeconds 过期时间，以秒为单位
     * @return 返回true表示可以使用；false表示已经存在，不能使用
     */
    boolean uniqueRequest(String appId, String nonce, int expiresInSeconds);

}
