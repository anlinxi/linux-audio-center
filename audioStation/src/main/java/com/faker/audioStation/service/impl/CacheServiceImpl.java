package com.faker.audioStation.service.impl;

import cn.hutool.core.convert.Convert;
import com.alibaba.fastjson.JSONObject;
import com.faker.audioStation.enums.PathEnum;
import com.faker.audioStation.model.dto.CacheDto;
import com.faker.audioStation.service.CacheService;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * <p>缓存服务</p>
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
@Slf4j
@Service
public class CacheServiceImpl implements CacheService {

    /**
     * concurrent包的线程安全Map，用来存放每个客户端对应的MyWebSocket对象。
     */
    private static ConcurrentHashMap<String, CacheDto> webSocketMap = new ConcurrentHashMap<>();

    @ApiModelProperty("是否初始化")
    private Boolean initialized = false;

    @Value("${faker.resources:/music/}")
    @ApiModelProperty("资源文件路径")
    private String resourcePath;

    /**
     * 设置值
     *
     * @param key
     * @param value
     * @param timeout
     * @param unit
     */
    @Override
    public void set(String key, Object value, long timeout, TimeUnit unit) {
        CacheDto cacheDto = new CacheDto();
        cacheDto.setKey(key);
        cacheDto.setValue(value);
        cacheDto.setClassz(value.getClass());
        cacheDto.setInitTime(System.currentTimeMillis());
        cacheDto.setOverTime(timeout);
        cacheDto.setTimeUnit(unit);
        webSocketMap.put(key, cacheDto);
    }

    /**
     * 设置值
     *
     * @param key
     * @param value
     * @param timeout
     */
    @Override
    public void set(String key, Object value, long timeout) {
        set(key, value, timeout, TimeUnit.MINUTES);
    }

    /**
     * 设置值
     *
     * @param key
     * @param value
     */
    @Override
    public void set(String key, Object value) {
        set(key, value, 30);
    }

    /**
     * 获取缓存值
     *
     * @param key
     * @return
     */
    @Override
    public <T> T get(String key) {
        CacheDto cacheDto = webSocketMap.get(key);
        if (null == cacheDto) {
            return null;
        }
        return (T) cacheDto.getValue();
    }

    /**
     * 获取过期时间
     *
     * @param key
     * @param timeUnit
     * @return
     */
    @Override
    public Long getExpire(String key, TimeUnit timeUnit) {
        CacheDto cacheDto = webSocketMap.get(key);
        if (null == cacheDto) {
            return null;
        }
        long milliSeconds = Convert.convertTime(cacheDto.getOverTime(), cacheDto.getTimeUnit(), TimeUnit.MILLISECONDS);
        long expire = cacheDto.getInitTime() + milliSeconds - System.currentTimeMillis();
        return Convert.convertTime(expire, TimeUnit.MILLISECONDS, timeUnit);
    }

    /**
     * 删除键值
     *
     * @param key
     */
    @Override
    public void delete(String key) {
        webSocketMap.remove(key);
    }


    /**
     * 根据正则表达式查询键
     *
     * @param redisKey
     * @return
     */
    @Override
    public Set<String> keys(String redisKey) {
        if (redisKey.endsWith("*")) {
            redisKey = redisKey.substring(0, redisKey.length() - 1);
        }
        Set<String> keys = new HashSet<String>();
        for (String key : webSocketMap.keySet()) {
            if (key != null && key.startsWith(redisKey)) {
                keys.add(key);
            }
        }
        return keys;
    }

    /**
     * 清理过期缓存
     */
    @Override
    public void cleanCache() {
        String cachePath = resourcePath + PathEnum.CACHE_JSON_PATH.getPath();
        if (!initialized) {
            File cacheFile = new File(cachePath + "cache.xlh");
            if (!cacheFile.getParentFile().exists()) {
                cacheFile.getParentFile().mkdirs();
            }
            if (cacheFile.exists()) {
                try {
                    FileInputStream fis = new FileInputStream(cacheFile);
                    //创建对象输入流
                    ObjectInputStream ois = new ObjectInputStream(fis);
                    //读取对象
                    webSocketMap = (ConcurrentHashMap) ois.readObject();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            initialized = true;
        }
        Iterator<String> it = webSocketMap.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            CacheDto cacheDto = webSocketMap.get(key);
            long milliSeconds = Convert.convertTime(cacheDto.getOverTime(), cacheDto.getTimeUnit(), TimeUnit.MILLISECONDS);
            long expire = cacheDto.getInitTime() + milliSeconds - System.currentTimeMillis();
            if (expire < 0) {
                // 这个可以实现 遍历的过程中删除某个元素
                log.info("过期已删除" + cacheDto.getKey() + "(" + cacheDto.getClassz().getSimpleName() + ")");
                it.remove();
            }
        }
        try {
            FileWriter writer = new FileWriter(cachePath + "cache.json");
            writer.write(JSONObject.toJSONString(webSocketMap));
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            FileOutputStream fos = new FileOutputStream(cachePath + "cache.xlh");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            //调用 ObjectOutputStream 中的 writeObject() 方法 写对象
            oos.writeObject(webSocketMap);
            //flush方法刷新缓冲区，写字符时会用，因为字符会先进入缓冲区，将内存中的数据立刻写出
            oos.flush();
            fos.close();
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
