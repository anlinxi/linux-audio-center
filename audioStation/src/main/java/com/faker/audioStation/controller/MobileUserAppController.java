package com.faker.audioStation.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.faker.audioStation.model.domain.JsMobileUser;
import com.faker.audioStation.service.CacheService;
import com.faker.audioStation.service.IJsMobileUserService;
import com.faker.audioStation.util.DesUtils;
import com.faker.audioStation.util.IdGen;
import com.faker.audioStation.util.RandomValidateCodeUtil;
import com.faker.audioStation.util.ToolsUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 移动端用户表Controller
 *
 * @author 淡梦如烟
 * @version 2020-07-25
 */
@Controller
@Api("移动端登录鉴权方法")
@RequestMapping(value = "app/")
public class MobileUserAppController extends BaseController {


    /**
     * 移动端用户表服务层
     */
    @Autowired
    private IJsMobileUserService iJsMobileUserService;


    @Autowired
    private CacheService cacheService;

    @ApiModelProperty("登录des加密参数")
    public static String DES_SECRET_KEY = "tx,anlinxi,top";

    @ResponseBody
    @ApiOperation(value = "移动端登录", notes = "移动端登录方法")
    @RequestMapping(value = "login")
    public String login(String username, String password, String __sid, HttpServletRequest request) {
        JSONObject result = new JSONObject();
        result.put("message", "登录失败");
        result.put("result", "false");
        if (StringUtils.isEmpty(password)) {
            result.put("message", "密码不能为空");
            result.put("isValidCodeLogin", false);
            return result.toString();
        }
        if (null == __sid || "".equals(__sid)) {
            __sid = IdGen.uuid();
        }
        result.put("sessionid", __sid);

        //防暴力破解
        String loginTimesKey = "loginTimes:" + username;
        String n = cacheService.get(loginTimesKey);
        Long t = cacheService.getExpire(loginTimesKey, TimeUnit.MINUTES);
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(n)) {
            if (Integer.parseInt(n) > 6 && t != null && t > 0) {
                result.put("message", "密码错误超过最大次数限制,还需等待" + t + "分钟");
                result.put("isValidCodeLogin", false);
                return result.toString();
            }
        }

        // 登录密码解密（解决密码明文传输安全问题）
        String secretKey = DES_SECRET_KEY;
        QueryWrapper<JsMobileUser> mobileUser = new QueryWrapper<>();
        String loginCode = DesUtils.decode(username, secretKey);
        if (StringUtils.isNotBlank(secretKey)) {
            mobileUser.eq("login_code", loginCode);
            mobileUser.eq("password", password);
        }

        JsMobileUser user1 = iJsMobileUserService.getOne(mobileUser);
        if (null != user1 && null != user1.getLoginCode() && null != user1.getPassword()) {
            result.put("message", "登录成功！");
            result.put("result", "true");
            String uuid = iJsMobileUserService.getTokenByUserId(user1.getLoginCode());
            result.put("sessionid", uuid);
            user1.setLastLoginDate(new Date());
            user1.setLastLoginIp(request.getRemoteAddr());
            user1.setExtendI1(1L);
            iJsMobileUserService.updateById(user1);
            JsMobileUser user2 = new JsMobileUser();
            BeanUtils.copyProperties(user1, user2);
            user2.setPassword("");
            result.put("user", user2);
            cacheService.set("mobileLogin:" + user1.getUserCode(), user1, 36, TimeUnit.HOURS);
        } else {
            String num = cacheService.get(loginTimesKey);
            int times = 1;
            if (org.apache.commons.lang3.StringUtils.isNotEmpty(num)) {
                times = Integer.parseInt(num) + 1;
            }
            cacheService.set(loginTimesKey, times + "", 60 * 60 * 1000, TimeUnit.MILLISECONDS);
            result.put("message", "账号或密码错误，请重试。");
            result.put("username", loginCode);
            result.put("isValidCodeLogin", false);
        }
        return result.toString();
    }

    @ApiOperation(value = "注销登录", notes = "注销的方法")
    @RequestMapping(value = "logout")
    @ResponseBody
    public Map loginOut(String username, String __sid) {
        Map result = new HashMap();
        result.put("message", "未知异常");
        result.put("result", "false");
        try {
            cacheService.delete("mobileLogin:" + username);
            result.put("message", "你已注销！");
            result.put("result", "true");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("message", "没有登录信息，已注销！");
            result.put("result", "true");
        }
        return result;
    }

    /**
     * 注册的方法
     *
     * @param loginCode 用户名
     * @param password  密码
     * @param userName  昵称
     * @param __sid     token
     * @return
     */
    @ApiOperation(value = "注册的方法", notes = "注册的方法")
    @RequestMapping(value = "register")
    @ResponseBody
    public Map register(@ApiParam(value = "用户名") String loginCode, @ApiParam(value = "密码") String password, @ApiParam(value = "昵称") String userName, @ApiParam(value = "token") String __sid) {
        // 参数组装对象
        JsMobileUser mobileUser = new JsMobileUser();
        mobileUser.setLoginCode(loginCode);
        mobileUser.setPassword(password);
        mobileUser.setUserName(userName);
        Map result = new HashMap();
        result.put("message", "未知异常");
        result.put("result", "false");
        try {
            // 用户名非空验证
            if (ToolsUtil.isNullOrEmpty(mobileUser.getLoginCode())) {
                result.put("message", "用户名不能为空！");
            }
            // 密码非空验证
            if (ToolsUtil.isNullOrEmpty(mobileUser.getPassword())) {
                result.put("message", "密码不能为空！");
            }
            // 登录密码解密（解决密码明文传输安全问题）
            String secretKey = DES_SECRET_KEY;
            String realUserName = DesUtils.decode(mobileUser.getLoginCode(), secretKey);
            // 用户名不能重名
            JsMobileUser mobileUser2 = iJsMobileUserService.getById(realUserName);
            if (mobileUser2 != null && ToolsUtil.isNotNull(mobileUser2.getLoginCode())) {
                result.put("message", "该用户名已注册！");
                return result;
            } else {
                // 新增用户
                Date now = new Date();
                mobileUser.setLoginCode(realUserName);
                mobileUser.setCreateDate(now);
                mobileUser.setCreateBy(realUserName);
                mobileUser.setUpdateDate(now);
                mobileUser.setUpdateBy(realUserName);
                String uuid = IdGen.uuid();
                mobileUser.setUserCode(realUserName + "_" + uuid);
                mobileUser.setToken(uuid);
                mobileUser.setUserType("employee");
                mobileUser.setMgrType("0");
                mobileUser.setStatus("0");
                iJsMobileUserService.save(mobileUser);

                result.put("sessionid", uuid);
                result.put("user", mobileUser);
                result.put("message", "注册成功！");
                result.put("result", "true");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("message", "注册异常:" + e.getMessage());
            result.put("result", "false");
        }
        return result;
    }

    @ApiOperation(value = "获取验证码的方法")
    @GetMapping(value = "/getVerify")
    public void getVerify(HttpServletRequest request, HttpServletResponse response) {
        try {
            String currentuserid = request.getParameter("userId");
            if (null == currentuserid || "".equals(currentuserid)) {
                return;
            }
            response.setContentType("image/jpeg");
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expire", 0);
            RandomValidateCodeUtil randomValidateCode = new RandomValidateCodeUtil();
            //返回验证码
            String randomString = randomValidateCode.getRandcode(request, response);
            log.info("用户[" + currentuserid + "]验证码[" + randomString + "]");
            //将随机码存入reids,和登录账号绑定用于登录校验
            cacheService.set(RandomValidateCodeUtil.RANDOMCODEKEY + ":" + currentuserid, randomString, RandomValidateCodeUtil.RANDOMVALIDATETIME, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
