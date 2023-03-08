package com.faker.audioStation.controller;

import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.lang.reflect.Method;

public class BaseController {

    /**
     * 日志对象
     */
    protected Logger log = LoggerFactory.getLogger(this.getClass());


    /**
     * 获取当前执行的方法
     *
     * @return 方法对象
     */
    protected Method getMethodApi() {
        String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
        Method[] methods = this.getClass().getMethods();
        Method thisMethod = null;
        for (Method method : methods) {
            if (null != methods && method.getName().equals(methodName)) {
                thisMethod = method;
                break;
            }
        }
        return thisMethod;
    }

    /**
     * get请求封装的日志
     *
     * @return
     */
    protected String getMethodLog() {
        String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
        Method[] methods = this.getClass().getMethods();
        Method thisMethod = null;
        for (Method method : methods) {
            if (null != methods && method.getName().equals(methodName)) {
                thisMethod = method;
                break;
            }
        }
        String _title = thisMethod.getAnnotation(ApiOperation.class).value();
        String _url = thisMethod.getAnnotation(GetMapping.class).value()[0];
        return new String("进入GET方法[" + _title + "],请求路径[" + _url + "]:");
    }

    /**
     * post请求封装的日志
     *
     * @return
     */
    protected String postMethodLog() {
        String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
        Method[] methods = this.getClass().getMethods();
        Method thisMethod = null;
        for (Method method : methods) {
            if (null != methods && method.getName().equals(methodName)) {
                thisMethod = method;
                break;
            }
        }
        String _title = thisMethod.getAnnotation(ApiOperation.class).value();
        String _url = thisMethod.getAnnotation(PostMapping.class).value()[0];
        return new String("进入POST方法[" + _title + "],请求路径[" + _url + "]:");
    }

    /**
     * 获取swagger2的注解value值
     *
     * @return
     */
    protected String getApiOperationTitle() {
        String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
        Method[] methods = this.getClass().getMethods();
        Method thisMethod = null;
        for (Method method : methods) {
            if (null != methods && method.getName().equals(methodName)) {
                thisMethod = method;
                break;
            }
        }
        return thisMethod.getAnnotation(ApiOperation.class).value();
    }
}
