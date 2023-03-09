package com.faker.audioStation.test;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import top.yumbo.util.music.musicImpl.netease.NeteaseCloudMusicInfo;

/**
 * <p>网易云音乐api测试</p>
 *
 * <p>项目名称：audioCenter</p>
 *
 * <p>注释:无</p>
 *
 * <p>Copyright: Copyright Faker(c) 2023/3/9</p>
 *
 * <p>公司: Faker</p>
 *
 * @author 淡梦如烟
 * @version 1.0
 * @date 2023/3/9 17:52
 */
@Slf4j
public class NeteaseCloudMusicInfoApiTest {

    /**
     * 模糊查询歌曲
     *
     * @throws Exception
     */
    @Test
    public void test1() throws Exception {
        NeteaseCloudMusicInfo neteaseCloudMusicInfo = new NeteaseCloudMusicInfo();
        JSONObject searJsonObject = new JSONObject();
        searJsonObject.put("keywords", "海阔天空");
        searJsonObject.put("type", "1");
        searJsonObject.put("limit", "1");
        JSONObject searJsonResult = neteaseCloudMusicInfo.search(searJsonObject);
        log.info(searJsonResult.toString());
    }

    /**
     * 根据id查询歌词
     *
     * @throws Exception
     */
    @Test
    public void test2() throws Exception {
        NeteaseCloudMusicInfo neteaseCloudMusicInfo = new NeteaseCloudMusicInfo();
        JSONObject searJsonObject = new JSONObject();
        searJsonObject.put("id", "347230");
        JSONObject searJsonResult = neteaseCloudMusicInfo.lyric(searJsonObject);
        log.info(searJsonResult.toString());
    }

    /**
     * 根据id查询歌手信息
     *
     * @throws Exception
     */
    @Test
    public void test3() throws Exception {
        NeteaseCloudMusicInfo neteaseCloudMusicInfo = new NeteaseCloudMusicInfo();
        JSONObject searJsonObject = new JSONObject();
        searJsonObject.put("id", "11127");
        JSONObject searJsonResult = neteaseCloudMusicInfo.artistDetail(searJsonObject);
        log.info(searJsonResult.toString());
    }

    /**
     * 根据id查询歌手信息
     *
     * @throws Exception
     */
    @Test
    public void test4() throws Exception {
        NeteaseCloudMusicInfo neteaseCloudMusicInfo = new NeteaseCloudMusicInfo();
        JSONObject searJsonObject = new JSONObject();
        searJsonObject.put("id", "347230");
        JSONObject searJsonResult = neteaseCloudMusicInfo.commentMusic(searJsonObject);
        log.info(searJsonResult.toString());
    }
}
