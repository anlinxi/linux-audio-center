package com.faker.audioStation.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * <p>RunCmdTest</p>
 *
 * <p>项目名称：haveFunCenter</p>
 *
 * <p>注释:无</p>
 *
 * <p>Copyright: Copyright Faker(c) 2022/10/7</p>
 *
 * <p>公司: Faker</p>
 *
 * @author 淡梦如烟
 * @version 1.0
 * @date 2022/10/7 12:48
 */
@Slf4j
public class RunCmdTest {

    @Test
    public void cmd1() throws Exception {
        String cmd = "D:\\javaTools\\ffmpeg-master-latest-win64-gpl\\bin\\ffmpeg -y -i M:\\haveFunTemp\\果冻传媒91YCM-022约炮约到相亲对像\\果冻传媒91YCM-022约炮约到相亲对像.ts -acodec copy -vcodec copy -f mp4 \\\\192.168.123.223\\web\\faker\\data\\video\\果冻传媒91YCM-022约炮约到相亲对像.mp4";
        cmd = "java -version";
        String classPath = this.getClass().getResource("/").getPath();
        log.info("classPath:" + classPath);
        cmd = "D:\\javaTools\\ffmpeg-master-latest-win64-gpl\\bin\\ffmpeg -y -i M:\\haveFunTemp\\果冻传媒91YCM-022约炮约到相亲对像\\果冻传媒91YCM-022约炮约到相亲对像.ts -acodec copy -vcodec copy -f mp4 M:\\haveFunTemp\\果冻传媒91YCM-022约炮约到相亲对像.mp4";
        log.info("命令:" + cmd);
        Process process = Runtime.getRuntime().exec(cmd);

        // 通过读取进程的流信息，可以看到视频转码的相关执行信息，并且使得下面的视频转码时间贴近实际的情况
        BufferedReader br = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        String line = null;
        while ((line = br.readLine()) != null) {
            log.info(line);
        }
        br.close();
        process.getOutputStream().close();
        process.getInputStream().close();
        process.getErrorStream().close();
        process.waitFor();
        process.destroy();

    }
}
