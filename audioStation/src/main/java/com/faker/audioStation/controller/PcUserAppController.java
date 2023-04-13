package com.faker.audioStation.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.faker.audioStation.model.domain.JsMobileUser;
import com.faker.audioStation.model.dto.LoginDto;
import com.faker.audioStation.service.CacheService;
import com.faker.audioStation.service.IJsMobileUserService;
import com.faker.audioStation.util.DesUtils;
import com.faker.audioStation.util.RandomValidateCodeUtil;
import com.faker.audioStation.wrapper.AesWrapMapper;
import com.faker.audioStation.wrapper.Wrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 移动端用户表Controller
 *
 * @author 淡梦如烟
 * @version 2020-07-25
 */
@Controller
@Api("web端登录鉴权方法")
@RequestMapping(value = "pc/")
public class PcUserAppController extends MobileUserAppController {

    /**
     * 移动端用户表服务层
     */
    @Autowired
    private IJsMobileUserService iJsMobileUserService;


    @Autowired
    private CacheService cacheService;

    @ResponseBody
    @ApiOperation(value = "pc端登录", notes = "web登录方法")
    @PostMapping(value = "login")
    public Wrapper login(@RequestBody LoginDto params, HttpServletRequest request) {
        log.info(super.postMethodLog());
        log.info(params.toString());
        String username = params.getUsername();
        String password = params.getPassword();
        String randomcode = params.getVercode();
        if (StringUtils.isEmpty(password)) {
            return AesWrapMapper.error("密码不能为空!");
        }
        //验证码校验
        String verifyCode = cacheService.get(RandomValidateCodeUtil.RANDOMCODEKEY + ":" + username);
        if ((randomcode == null || !randomcode.equalsIgnoreCase(verifyCode))) {
            cacheService.delete(RandomValidateCodeUtil.RANDOMCODEKEY + ":" + username);
            return AesWrapMapper.error("验证码错误或过期, 请重新输入");
        }

        //防暴力破解
        String loginTimesKey = "loginTimes:" + username;
        String n = cacheService.get(loginTimesKey);
        Long t = cacheService.getExpire(loginTimesKey, TimeUnit.MINUTES);
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(n)) {
            if (Integer.parseInt(n) > 6 && t != null && t > 0) {
                cacheService.delete(RandomValidateCodeUtil.RANDOMCODEKEY + ":" + username);
                return AesWrapMapper.error("密码错误超过最大次数限制,还需等待" + t + "分钟");
            }
        }

        QueryWrapper<JsMobileUser> mobileUser = new QueryWrapper<>();
        mobileUser.eq("login_code", username);
//        mobileUser.eq("password", SecureUtil.md5(password));

        JsMobileUser user1 = iJsMobileUserService.getOne(mobileUser);
        //比较密码
        String md5Password = null;
        if (null != user1) {
            // 登录密码解密（解决密码明文传输安全问题）
            md5Password = DesUtils.encode(password, DES_SECRET_KEY);
            log.info("用户[" + user1.getLoginCode() + "]数据库密码[" + user1.getPassword() + "],输入密码[" + password + "]转码后[" + md5Password + "]");
        }
        if (null != user1 && null != user1.getLoginCode() && null != user1.getPassword() && user1.getPassword().equals(md5Password)) {
            String uuid = iJsMobileUserService.getTokenByUserId(user1.getLoginCode());
            user1.setLastLoginDate(new Date());
            user1.setLastLoginIp(request.getRemoteAddr());
            user1.setExtendI1(1L);
            iJsMobileUserService.updateById(user1);
            JsMobileUser user2 = new JsMobileUser();
            BeanUtils.copyProperties(user1, user2);
            user2.setPassword("");
            user2.setToken(uuid);
            cacheService.set("mobileLogin:" + user1.getUserCode(), user1, 36, TimeUnit.DAYS);
            cacheService.delete(RandomValidateCodeUtil.RANDOMCODEKEY + ":" + username);
            return AesWrapMapper.ok(user2);
        } else {
            String num = cacheService.get(loginTimesKey);
            int times = 1;
            if (org.apache.commons.lang3.StringUtils.isNotEmpty(num)) {
                times = Integer.parseInt(num) + 1;
            }
            cacheService.set(loginTimesKey, times + "", 60 * 60 * 1000, TimeUnit.MILLISECONDS);
            cacheService.delete(RandomValidateCodeUtil.RANDOMCODEKEY + ":" + username);
            return AesWrapMapper.error("用户名或密码错误!");
        }
    }

}
