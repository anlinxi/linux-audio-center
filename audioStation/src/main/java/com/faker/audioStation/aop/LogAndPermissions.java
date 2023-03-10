package com.faker.audioStation.aop;

import java.lang.annotation.*;

/**
 * <p>日志信息和用户角色权限注解</p>
 *
 * <p>项目名称：lnwlcsMicroService</p>
 *
 * <p>注释:需要配合swagger注解和@PostMapping/@GetMapping注解使用</p>
 *
 * <p>Copyright: Copyright Faker(c) 2022/12/30</p>
 *
 * <p>公司: Faker</p>
 *
 * @author 淡梦如烟
 * @version 1.0
 * @date 2022/12/30 9:55
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogAndPermissions {

    /**
     * 角色权限编码
     *
     * @return
     */
    String[] value() default {};


    /**
     * 是否要打印方法日志
     *
     * @return
     */
    boolean isLoggable() default true;
}
