package com.faker.audioStation.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.faker.audioStation.model.domain.JsMobileUser;

/**
 * <p>
 * 移动端用户表 服务类
 * </p>
 *
 * @author anlin
 * @since 2022-07-26
 */
public interface IJsMobileUserService extends IService<JsMobileUser> {

    /**
     * 通过token反查userId
     *
     * @param token
     * @return
     */
    String getUserIdByToken(String token);

    /**
     * 生成token
     *
     * @param loginCode
     * @return
     */
    String getTokenByUserId(String loginCode);
}
