package com.faker.audioStation.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@ApiIgnore
public class Swagger2HtmlController {
    /**
     * 日志对象
     */
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 映射跳转页面
     *
     * @return
     */
    @RequestMapping("/")
    public ModelAndView index() {
        logger.info("重定向到Swagger2页面...");
//        ModelAndView mav = new ModelAndView("redirect:/doc.html"); // 绝对路径OK
        ModelAndView mav = new ModelAndView("redirect:/login.html"); // 绝对路径OK
        return mav;
    }
}
