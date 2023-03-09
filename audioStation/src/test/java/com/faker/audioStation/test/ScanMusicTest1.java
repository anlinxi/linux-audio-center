package com.faker.audioStation.test;

import com.faker.audioStation.conf.SqliteInit;
import com.faker.audioStation.scanner.Scanner;
import com.faker.audioStation.util.SpringContextUtils;
import com.faker.audioStation.wrapper.Wrapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class ScanMusicTest1 {

    @Autowired
    Scanner scanner;


    @Test
    public void test1() throws Exception {
        Wrapper wrapper = scanner.startScan("E:\\CloudMusic");
        log.info(wrapper.toString());
    }
}
