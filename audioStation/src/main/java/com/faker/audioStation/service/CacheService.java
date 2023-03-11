package com.faker.audioStation.service;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * <p>CacheService</p>
 *
 * <p>项目名称：audioCenter</p>
 *
 * <p>注释:无</p>
 *
 * <p>Copyright: Copyright Faker(c) 2023/3/8</p>
 *
 * <p>公司: Faker</p>
 *
 * @author 淡梦如烟
 * @version 1.0
 * @date 2023/3/8 10:21
 */
public interface CacheService {

    /**
     * 设置值
     *
     * @param key
     * @param value
     * @param timeout
     * @param unit
     */
    public void set(String key, Serializable value, long timeout, TimeUnit unit);

    /**
     * 设置值
     *
     * @param key
     * @param value
     * @param timeout
     */
    public void set(String key, Serializable value, long timeout);

    /**
     * 设置值
     *
     * @param key
     * @param value
     */
    public void set(String key, Serializable value);

    /**
     * 获取缓存值
     *
     * @param key
     * @param <T>
     * @return
     */
    public <T> T get(String key);

    /**
     * 获取过期时间
     *
     * @param key
     * @param timeUnit
     * @return
     */
    Long getExpire(String key, TimeUnit timeUnit);

    /**
     * 删除键值
     *
     * @param key
     */
    void delete(String key);

    /**
     * 根据正则表达式查询键
     *
     * @param redisKey
     * @return
     */
    Set<String> keys(String redisKey);

    /**
     * 清理过期缓存
     */
    void cleanCache();

    /**
     * 获取所有缓存key参数
     *
     * @return
     */
    List<String> allCacheKeys();
}
