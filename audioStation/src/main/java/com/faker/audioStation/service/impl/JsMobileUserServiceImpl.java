package com.faker.audioStation.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.faker.audioStation.mapper.JsMobileUserMapper;
import com.faker.audioStation.model.domain.JsMobileUser;
import com.faker.audioStation.service.CacheService;
import com.faker.audioStation.service.IJsMobileUserService;
import com.faker.audioStation.util.IdGen;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 移动端用户表 服务实现类
 * </p>
 *
 * @author anlin
 * @since 2022-07-26
 */
@Service
public class JsMobileUserServiceImpl extends ServiceImpl<JsMobileUserMapper, JsMobileUser> implements IJsMobileUserService {

    @Autowired
    private CacheService cacheService;

    @ApiModelProperty("用户token缓存")
    private Map<String, String> userTokenMap = new HashMap<String, String>();

    /**
     * 通过token反查userId
     *
     * @param token
     * @return
     */
    @Override
    public String getUserIdByToken(String token) {
        if (userTokenMap.get(token) != null) {
            return userTokenMap.get(token);
        }
        String redisKey = "userInfo:";
        Set<String> userSet = cacheService.keys(redisKey);
        for (String userId : userSet) {
            String userToken = cacheService.get(userId);
            if (userToken != null) {
                userTokenMap.put(userToken, userId.substring(redisKey.length()));
                break;
            }
        }
        return userTokenMap.get(token);
    }

    /**
     * 生成token
     *
     * @param userId
     * @return
     */
    @Override
    public String getTokenByUserId(String userId) {
        try {
            String token = IdGen.uuid();
            JsMobileUser update = new JsMobileUser();
            update.setUserCode(userId);
            update.setToken(token);
            this.updateById(update);
            String redisKey = "userInfo:" + userId;
            cacheService.set(redisKey, token, 60, TimeUnit.HOURS);
            userTokenMap.put(redisKey, userId);
            return token;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
