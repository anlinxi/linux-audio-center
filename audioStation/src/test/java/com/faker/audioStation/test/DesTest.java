package com.faker.audioStation.test;

import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class DesTest {


    @Test
    public void test1() throws Exception {
        String secretKey = "tx,anlinxi,top";
        SymmetricCrypto des = new SymmetricCrypto(SymmetricAlgorithm.DES, secretKey.getBytes());
        String loginCode = des.encryptBase64("web");
        log.info("Login code: " + loginCode);
    }
}
