package com.faker.audioStation;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;

import java.net.InetAddress;


@SpringBootApplication
@Slf4j
public class AudioCenterApplication {


    public static void main(String[] args) throws Exception {
        SpringApplication app = new SpringApplication(AudioCenterApplication.class);
        Environment env = app.run(args).getEnvironment();
        String ip = InetAddress.getLocalHost().getHostAddress();
        String port = env.getProperty("server.port");
        String property = env.getProperty("server.servlet.context-path");
        String path = property == null ? "" : property;
        log.info(
                "\n\t" +
                        "----------------------------------------------------------\n\t" +
                        "项目已经启动:\n\t" +
                        "本地本地路径: \thttp://localhost:" + port + path + "/\n\t" +
                        "外网访问路径: \thttp://" + ip + ":" + port + path + "/\n\t" +
                        "------------------------------------------------------------");

    }

}
