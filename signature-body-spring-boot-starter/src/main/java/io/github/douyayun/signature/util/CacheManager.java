package io.github.douyayun.signature.util;

import com.google.common.cache.*;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 功能描述
 */
@Slf4j
public class CacheManager {

    /**
     * 缓存项最大数量
     */
    private static final long GUAVA_CACHE_SIZE = Long.MAX_VALUE;

    /**
     * 缓存时间(单位秒)：1天
     */
    private static long GUAVA_CACHE_SECONDS = 60 * 60 * 24;
    /**
     * 缓存操作对象
     */
    private static LoadingCache<String, String> GLOBAL_CACHE = null;

    /**
     * 私有实例
     */
    private static CacheManager instance;

    public CacheManager() {
        try {
            GLOBAL_CACHE = loadCache(new CacheLoader<String, String>() {
                @Override
                public String load(String key) throws Exception {
                    // 处理缓存键不存在缓存值时的处理逻辑
                    return "";
                }
            });
        } catch (Exception e) {
            log.error("初始化Guava Cache出错", e);
        }
    }

    /**
     * 唯一公开获取实例的方法（静态工厂方法），该方法使用synchronized加锁，来保证线程安全性
     *
     * @return
     */
    public static synchronized CacheManager getInstance() {
        if (instance == null) {
            instance = new CacheManager();
        }
        return instance;
    }

    public static void setGuavaCacheSeconds(long guavaCacheSeconds) {
        GUAVA_CACHE_SECONDS = guavaCacheSeconds;
    }

    /**
     * 全局缓存设置
     * <p>
     * 缓存项最大数量：100000
     * 缓存有效时间（天）：10
     *
     * @param cacheLoader
     * @return
     * @throws Exception
     */
    private static LoadingCache<String, String> loadCache(CacheLoader<String, String> cacheLoader) throws Exception {
        LoadingCache<String, String> cache = CacheBuilder.newBuilder()
                //缓存池大小，在缓存项接近该大小时， Guava开始回收旧的缓存项
                .maximumSize(GUAVA_CACHE_SIZE)
                //设置时间对象没有被读/写访问则对象从内存中删除(在另外的线程里面不定期维护)
                .expireAfterAccess(GUAVA_CACHE_SECONDS, TimeUnit.SECONDS)
                // 设置缓存在写入之后 设定时间 后失效
                .expireAfterWrite(GUAVA_CACHE_SECONDS, TimeUnit.SECONDS)
                //移除监听器,缓存项被移除时会触发
                .removalListener(new RemovalListener<String, String>() {
                    @Override
                    public void onRemoval(RemovalNotification<String, String> rn) {
                        //逻辑操作
                        log.info("移除key：{}", rn.getKey());
                    }
                })
                //开启Guava Cache的统计功能
                .recordStats()
                .build(cacheLoader);
        return cache;
    }

    /**
     * 设置缓存值
     * 注: 若已有该key值，则会先移除(会触发removalListener移除监听器)，再添加
     *
     * @param key
     * @param value
     */
    public static void put(String key, String value) {
        try {
            GLOBAL_CACHE.put(key, value);
        } catch (Exception e) {
            log.error("设置缓存值出错", e);
        }
    }

    /**
     * 批量设置缓存值
     *
     * @param map
     */
    public static void putAll(Map<? extends String, ? extends String> map) {
        try {
            GLOBAL_CACHE.putAll(map);
        } catch (Exception e) {
            log.error("批量设置缓存值出错", e);
        }
    }

    /**
     * 获取缓存值
     * 注：如果键不存在值，将调用CacheLoader的load方法加载新值到该键中
     *
     * @param key
     * @return
     */
    public static String get(String key) {
        String token = "";
        try {
            token = GLOBAL_CACHE.get(key);
        } catch (Exception e) {
            log.error("获取缓存值出错", e);
        }
        return token;
    }

    /**
     * 移除缓存
     *
     * @param key
     */
    public static void remove(String key) {
        try {
            GLOBAL_CACHE.invalidate(key);
        } catch (Exception e) {
            log.error("移除缓存出错", e);
        }
    }

    /**
     * 批量移除缓存
     *
     * @param keys
     */
    public static void removeAll(Iterable<String> keys) {
        try {
            GLOBAL_CACHE.invalidateAll(keys);
        } catch (Exception e) {
            log.error("批量移除缓存出错", e);
        }
    }

    /**
     * 清空所有缓存
     */
    public static void removeAll() {
        try {
            GLOBAL_CACHE.invalidateAll();
        } catch (Exception e) {
            log.error("清空所有缓存出错", e);
        }
    }

    /**
     * 获取缓存项数量
     *
     * @return
     */
    public static long size() {
        long size = 0;
        try {
            size = GLOBAL_CACHE.size();
        } catch (Exception e) {
            log.error("获取缓存项数量出错", e);
        }
        return size;
    }
}
