package com.faker.audioStation.conf;

import com.faker.audioStation.service.CacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

//1.主要用于标记配置类，兼备Component的效果。
@Configuration
// 2.开启定时任务
@EnableScheduling
@Slf4j
public class SaticScheduleTask {


    @Autowired
    private CacheService cacheService;

    //3.添加定时任务
    @Scheduled(cron = "0/30 * * * * ?")
    //或直接指定时间间隔，例如：5秒
    //@Scheduled(fixedRate=5000)
    private void configureTasks() {
    }

    /**
     * 定时清理过期缓存
     */
    @Scheduled(fixedRate = 60 * 1000)
    private void cleanCache() {
        cacheService.cleanCache();
    }
}
