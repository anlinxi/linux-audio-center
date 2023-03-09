package com.faker.audioStation.test;

import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import com.faker.audioStation.scanner.Scanner;
import com.faker.audioStation.scanner.music.MusicScanner;
import com.faker.audioStation.wrapper.Wrapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class ScanMusicTest1 {

    @Autowired
    Scanner scanner;

    @Test
    public void test1() throws Exception {
        if (scanner == null) {
            scanner = new MusicScanner();
        }
        Wrapper wrapper = scanner.startScan("E:\\CloudMusic");
        log.info(wrapper.toString());
    }
}
