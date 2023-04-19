package io.github.douyayun.signature.util;

import java.io.Serializable;

/**
 * TODO
 *
 * @author houp
 * @since 1.0.0
 **/
public class LocalCacheEntity implements Serializable {

    private static final long serialVersionUID = -107853226360392750L;

    /**
     * 值
     */
    private Object value;

    /**
     * 保存的时间戳(秒)
     */
    private long gmtModify;

    /**
     * 过期时间（秒）
     */
    private int expire;

    public LocalCacheEntity(Object value, long gmtModify, int expire) {
        super();
        this.value = value;
        this.gmtModify = gmtModify;
        this.expire = expire;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public long getGmtModify() {
        return gmtModify;
    }

    public void setGmtModify(long gmtModify) {
        this.gmtModify = gmtModify;
    }

    public int getExpire() {
        return expire;
    }

    public void setExpire(int expire) {
        this.expire = expire;
    }

}