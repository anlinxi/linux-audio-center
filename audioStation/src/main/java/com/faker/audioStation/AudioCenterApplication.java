package com.faker.audioStation;

import com.faker.audioStation.util.TestStatut;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
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
        //网易云音乐API地址
        String music163Api = env.getProperty("faker.music163Api");
        String path = property == null ? "" : property;
        TestStatut.ROOT_URL = "http://" + ip + ":" + port + path + "";
        log.info(
                "\n\t" +
                        "----------------------------------------------------------\n\t" +
                        "项目已经启动:\n\t" +
                        "本地本地路径: \thttp://localhost:" + port + path + "/\n\t" +
                        "外网访问路径: \thttp://" + ip + ":" + port + path + "/\n\t" +
                        "网易云音乐API:\t" + music163Api + "/\n\t" +
                        "------------------------------------------------------------");

    }

}
