package com.faker.audioStation.filter;


import com.alibaba.fastjson.JSONObject;
import com.faker.audioStation.model.domain.JsMobileUser;
import com.faker.audioStation.service.CacheService;
import com.faker.audioStation.service.IJsMobileUserService;
import com.faker.audioStation.util.SpringContextUtils;
import com.faker.audioStation.util.ToolsUtil;
import com.faker.audioStation.wrapper.WrapMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 移动端权限过滤器
 *
 * @author ThinkGem
 * @version 2020-4-13
 */
@Slf4j
public class MobileAuthFilter implements Filter {


    @Autowired
    private IJsMobileUserService iJsMobileUserService = null;

    @Autowired
    private CacheService cacheService;

    /**
     * 初始化
     * spring容器初始化bean对象的顺序是listener-->filter-->servlet
     * 所以需要spring需在这里手动注入
     *
     * @param filterConfig 过滤器设置
     * @throws ServletException servlet异常
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.warn("初始化移动端权限过滤器>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        if (null == iJsMobileUserService) {
            iJsMobileUserService = SpringContextUtils.getBean(IJsMobileUserService.class);
        }
        if (null == cacheService) {
            cacheService = SpringContextUtils.getBean(CacheService.class);
        }
    }

    /**
     * 执行过滤方法
     *
     * @param servletRequest  servlet请求
     * @param servletResponse servlet响应
     * @param filterChain     过滤器设置
     * @throws IOException      读写异常
     * @throws ServletException servlet异常
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        //例外
        String url = httpServletRequest.getRequestURL().toString();
        log.debug("[" + this.getClass().getName() + "]执行过滤方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n" + url);
        if (url.contains("/message-center/ws/") || url.endsWith("mobile/app_version_control/appVersion/update") || url.endsWith("mobile/app_version_control/appVersion/findLastVersionList")) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        /*
        nginx 配置
        underscores_in_headers on;
         */
        //获取当前用户
        String __token = httpServletRequest.getHeader("__token");
        String __userId = httpServletRequest.getHeader("__userId");
        if (null == __userId || "".equals(__userId) || null == __token || "".equals(__token)) {
            Map params = ToolsUtil.getAllParameterMap(httpServletRequest);
            if (null == __userId || "".equals(__userId)) {
                if (params.get("__userId") != null) {
                    __userId = String.valueOf(params.get("__userId"));
                }
            }
            if (null == __token || "".equals(__token)) {
                if (params.get("__token") != null) {
                    __token = String.valueOf(params.get("__token"));
                }
            }
        }

        String redisKey = "userInfo:" + __userId;
        if (null == __userId) {
            String errorMsg = "无用户验证消息";
            log.error(errorMsg);
            this.toLogin(httpServletRequest, httpServletResponse);
            return;
        }
        // 查看最近登录验证的次数
        String token = cacheService.get(redisKey);
        if (ToolsUtil.isNullOrEmpty(token)) {
            JsMobileUser jsMobileUser = iJsMobileUserService.getById(__userId);
            if (null != jsMobileUser) {
                token = jsMobileUser.getToken();
                cacheService.set(redisKey, token, 60, TimeUnit.HOURS);
            }
        }
        if (null != token) {
            if (token.equals(__token)) {
                log.debug("用户[" + __userId + "]app验证sid[" + __token + "]通过!");
            } else {
                String errorMsg = "[" + __userId + "]验证token未通过!token[" + token + "],sid[" + __token + "]";
                log.error(errorMsg);
                this.toLogin(httpServletRequest, httpServletResponse);
                return;
            }
        } else {
            if (null != __userId) {
                log.error("[" + __userId + "]还未登录!");
            } else {
                log.error("请求无登录用户名，阻止请求!" + url);
            }
            this.toLogin(httpServletRequest, httpServletResponse);
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    /**
     * 回复重新登录的信息
     *
     * @param httpServletRequest
     * @param httpServletResponse
     * @throws IOException
     */
    private void toLogin(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        //认证过了
        httpServletResponse.setContentType("application/json; charset=utf-8");
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        OutputStream out = httpServletResponse.getOutputStream();
        out.write(JSONObject.toJSONString(WrapMapper.wrap(401, "请重新登录!")).getBytes("UTF-8"));
    }
}
