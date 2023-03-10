package com.faker.audioStation.aop;


import com.faker.audioStation.model.domain.JsMobileUser;
import com.faker.audioStation.service.CacheService;
import com.faker.audioStation.wrapper.WrapMapper;
import com.faker.audioStation.wrapper.Wrapper;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * <p>控制层日志打印切面</p>
 *
 * <p>项目名称：lnwlcsMicroService</p>
 *
 * <p>注释:无</p>
 *
 * <p>Copyright: Copyright Faker(c) 2022/12/30</p>
 *
 * <p>公司: Faker</p>
 *
 * @author 淡梦如烟
 * @version 1.0
 * @date 2022/12/30 9:51
 */
@Aspect
@Component
@Slf4j
public class ControllerAspect {

    /**
     * 请求头token对应的key值
     */
    private static final String AUTHORIZATION = "__token";

    /**
     * 切面保存的信息
     */
    private static final ThreadLocal<LogAspectDto> threadLocal = new ThreadLocal<>();

    /**
     * 是否打印日志
     */
    private boolean isLoggable = true;

    /**
     * 获取用户id
     */
    private String userId = null;

    /**
     * 方法名
     */
    private String methodName = null;


    /**
     * 请求路径
     */
    private String url = null;

    @Autowired
    @ApiModelProperty("缓存服务")
    CacheService cacheService;

    /**
     * 切入点声明
     */
    @Pointcut("@annotation(com.faker.audioStation.aop.LogAndPermissions)")
    public void startControllere() {
    }

    /**
     * 进入方法前校验
     *
     * @param joinPoint pointcut
     */
    @Around("startControllere()")
    public Object beforeStartInvoke(ProceedingJoinPoint joinPoint) throws Throwable {
        LogAspectDto params = new LogAspectDto();
        params.setStartTime(new Date());
        threadLocal.set(params);

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            throw new RuntimeException("未知错误,获取attributes失败");
        }
        HttpServletRequest request = attributes.getRequest();
        String token = request.getHeader(AUTHORIZATION);
        if (StringUtils.isEmpty(token)) {
            log.error("请求头token为空!");
        }
        userId = request.getHeader("__userId");

        Object[] args = joinPoint.getArgs();
        JSONArray jsonArray = new JSONArray();
        for (Object arg : args) {
            if (arg != null
                    && !arg.getClass().equals(org.apache.catalina.connector.ResponseFacade.class)
                    && !arg.getClass().equals(org.apache.catalina.connector.RequestFacade.class)) {
                jsonArray.add(arg);
            }
        }
        //得到其方法签名
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
//        //获取方法参数类型数组
//        Class[] paramTypeArray = methodSignature.getParameterTypes();
        //参数名称
        String[] parameterNames = methodSignature.getParameterNames();
        //get多参数情况
        JSONObject sendParams = new JSONObject();
        for (int i = 0; i < jsonArray.size(); i++) {
            Object obj = jsonArray.get(i);
            if (obj != null) {
                if (obj instanceof String) {
                    String parameterName = parameterNames[i];
                    if ("userId".equals(parameterName)) {
                        userId = (String) obj;
                    }
                    sendParams.put(parameterName, obj);
                } else if (obj instanceof Long) {
                    sendParams.put(parameterNames[i], obj);
                } else if (obj instanceof Integer) {
                    sendParams.put(parameterNames[i], obj);
                } else {
                    JSONObject jsonObject = JSONObject.fromObject(obj);
                    if (null != jsonObject.get("userId")) {
                        userId = jsonObject.getString("userId");
                        break;
                    }
                }

            }

        }

        // 调用class 全名
        Class<?> clazz = joinPoint.getTarget().getClass();

        //日志文本
        StringBuffer logBefore = new StringBuffer();
        StringBuffer logEnd = new StringBuffer();

        //返回类型
        Class<?> returnType = null;
        try {
            if (null != userId) {
                logBefore.append("用户[" + userId + "]");
                logEnd.append("用户[" + userId + "]");
            }

            // 调用方法名
            methodName = joinPoint.getSignature().getName();
            Class<?>[] parameterTypes = ((MethodSignature) joinPoint.getSignature()).getMethod().getParameterTypes();
            Method method = clazz.getMethod(methodName, parameterTypes);
            returnType = method.getReturnType();
            LogAndPermissions logAndPermissions = method.getAnnotation(LogAndPermissions.class);
            if (logAndPermissions == null) {
                //一般不会进来
                log.error("未获取到@LogAndPermissions注解信息！");
                return WrapMapper.error("未获取到@LogAndPermissions注解信息！");
            }
            isLoggable = logAndPermissions.isLoggable();
            //todo 鉴权处理
            Wrapper permissionsWrapper = this.permissions(logAndPermissions, userId);
            if (permissionsWrapper.error()) {
                log.error(permissionsWrapper.toString());
                //不打印日志 直接返回了
                return permissionsWrapper;
            }
            if (isLoggable) {
                ApiOperation apiOperation = method.getAnnotation(ApiOperation.class);
                if (null != apiOperation) {
                    methodName = apiOperation.value();
                    if (apiOperation.notes() != null && !"".equals(apiOperation.notes())) {
                        methodName = apiOperation.value() + "(" + apiOperation.notes() + ")";
                    }
                }
                PostMapping postMapping = method.getAnnotation(PostMapping.class);
                if (postMapping != null) {
                    url = postMapping.value()[0];
                    logBefore.append("进入POST方法[" + methodName + "],请求路径[" + url + "]");
                    logEnd.append("离开POST方法[" + methodName + "],请求路径[" + url + "]");
                }
                GetMapping getMapping = method.getAnnotation(GetMapping.class);
                if (getMapping != null) {
                    url = getMapping.value()[0];
                    logBefore.append("进入GET方法[" + methodName + "],请求路径[" + url + "]");
                    logEnd.append("离开GET方法[" + methodName + "],请求路径[" + url + "]");
                }
                if (logEnd.length() <= 0) {
                    logBefore.append("进入方法[" + methodName + "]");
                    logEnd.append("离开方法[" + methodName + "]");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (isLoggable) {
            if (sendParams.size() > 0) {
                //get请求
                logBefore.append(",参数:" + sendParams.toString() + "");
            } else {
                //post请求
                logBefore.append(",参数:" + jsonArray.toString() + "");
            }

            log.info(logBefore.toString());
        }

        Object result = null;
        try {
            result = joinPoint.proceed(args);
        } catch (Throwable e) {
            //todo 异常返回要和方法的返回参数一直，不然会报错。也可以不在这里捕获异常
            if (null != returnType) {
                if (Wrapper.class.equals(returnType)) {
                    e.printStackTrace();
                    if (null != userId) {
                        result = WrapMapper.error("用户[" + userId + "]执行请求方法" + methodName + "异常:" + e.getClass().getSimpleName() + ":" + e.getMessage());
                    } else {
                        result = WrapMapper.error("执行请求方法" + methodName + "异常:" + e.getClass().getSimpleName() + ":" + e.getMessage());
                    }
                    return result;
                }
                throw e;
            } else {
                throw e;
            }
        }
        params.setEndTime(new Date());
        if (isLoggable) {
            Long usedLong = params.getUsedTime();
            if (usedLong != null) {
                logEnd.append(",耗时[" + formatTime(usedLong) + "]");
            }
            log.info(logEnd.toString());
        }
        return result;
    }

    /**
     * 正常返回
     *
     * @param joinPoint pointcut
     * @param ret       返回结果
     */
    @AfterReturning(value = "startControllere()", returning = "ret")
    public void afterReturning(JoinPoint joinPoint, Object ret) {
        threadLocal.remove();
    }

    /**
     * 抛出异常
     *
     * @param joinPoint pointcut
     * @param ex        异常
     */
    @AfterThrowing(value = "startControllere()", throwing = "ex")
    public void afterThrowing(JoinPoint joinPoint, Throwable ex) {
        StringBuffer logError = new StringBuffer();
        if (null != userId) {
            logError.append("用户[" + userId + "]");
        }
        logError.append("进入方法[" + methodName + "],请求路径[" + url + "]异常:" + ex.getMessage());
        threadLocal.remove();
    }

    /**
     * 将毫秒转为时分秒
     *
     * @param ms
     * @return
     */
    public String formatTime(Long ms) {
        Integer ss = 1000;
        Integer mi = ss * 60;
        Integer hh = mi * 60;
        Integer dd = hh * 24;

        Long day = ms / dd;
        Long hour = (ms - day * dd) / hh;
        Long minute = (ms - day * dd - hour * hh) / mi;
        Long second = (ms - day * dd - hour * hh - minute * mi) / ss;
        Long milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss;

        StringBuffer sb = new StringBuffer();
        if (day > 0) {
            sb.append(day + "天");
        }
        if (hour > 0) {
            sb.append(hour + "小时");
        }
        if (minute > 0) {
            sb.append(minute + "分");
        }
        if (second > 0) {
            sb.append(second + "秒");
        }
        if (milliSecond > 0) {
            sb.append(milliSecond + "毫秒");
        }
        return sb.toString();
    }


    /**
     * 根据用户信息进行鉴权
     *
     * @param logAndPermissions
     * @param userId
     * @return
     */
    private Wrapper permissions(LogAndPermissions logAndPermissions, String userId) {
        if (logAndPermissions == null || userId == null || "".equals(userId)) {
            return WrapMapper.ok("判断信息为空，不进行鉴权校验");
        }
        String[] rolesCodeArr = logAndPermissions.value();
        List<String> rolesCodeList = Arrays.asList(rolesCodeArr);
        if (rolesCodeArr == null || rolesCodeArr.length <= 0) {
            return WrapMapper.ok("没有角色权限编码，不进行鉴权校验");
        }
        //新版的角色表有多个
        List<String> roleIds = new ArrayList<>();

        //获取用户权限编码
        JsMobileUser jsMobileUser = cacheService.get("mobileLogin:" + userId);
        if (jsMobileUser == null || jsMobileUser.getLoginCode() == null) {
            return WrapMapper.ok("根据用户id[" + userId + "]未能查询导对应的用户信息，暂不进行鉴权校验");
        }
        if (jsMobileUser.getMgrType() != null) {
            //辽宁老版本只有dutyId,只有一个
            roleIds.add(String.valueOf(jsMobileUser.getMgrType()));
        }
        //开始校验
        for (String roleCodePermissions : rolesCodeList) {
            if (roleIds.contains(roleCodePermissions)) {
                return WrapMapper.ok("根据用户id[" + userId + "]角色编码" + roleIds + "已包含注解的权限编码[" + roleCodePermissions + "],鉴权通过");
            }
        }
        return WrapMapper.error("根据用户id[" + userId + "]角色编码" + roleIds + "未包含注解的权限编码" + rolesCodeList + ",鉴权失败");
    }

}
