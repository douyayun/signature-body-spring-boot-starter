package io.github.douyayun.signature.storage;

import io.github.douyayun.signature.util.LocalCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * TODO
 *
 * @author houp
 * @since 1.0.0
 */
public class MemoryNonceStorage implements NonceStorage, Serializable {
    private static final Logger log = LoggerFactory.getLogger(MemoryNonceStorage.class);

    public MemoryNonceStorage() {
        log.info("MemoryConfigStorage...");
    }

    /**
     * 唯一请求  锁定当前appId的nonce. 防止重放攻击
     *
     * @param appId            appId值
     * @param nonce            nonce值
     * @param expiresInSeconds 过期时间，以秒为单位
     * @return 返回true表示可以使用；false表示已经存在，不能使用
     */
    @Override
    public boolean uniqueRequest(String appId, String nonce, int expiresInSeconds) {
        String key = appId + ":" + nonce;
        if (!LocalCache.getInstance().containsKey(key)) {
            LocalCache.getInstance().putValue(key, "", expiresInSeconds);
            return true;
        }
        return false;
    }
}
