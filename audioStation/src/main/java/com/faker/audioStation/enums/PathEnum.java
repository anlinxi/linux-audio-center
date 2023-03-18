package com.faker.audioStation.enums;

/**
 * <p>资源路径枚举</p>
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
 * @date 2023/3/9 12:46
 */
public enum PathEnum {

    /**
     * 音乐封面目录
     */
    MUSIC_COVER("audioCenter/image/music"),

    /**
     * 歌手图片
     */
    SINGER_COVER("audioCenter/image/singer"),

    /**
     * 歌单图片
     */
    PLAYLIST_COVER("audioCenter/image/playlist"),

    /**
     * 歌词地址
     */
    LYRIC_PATH("audioCenter/lyric"),

    /**
     * 音乐地址
     */
    DOWNLOAD_MUSIC_PATH("audioCenter/music"),

    /**
     * 缓存持久化地址
     */
    CACHE_JSON_PATH("audioCenter/cache/"),

    /**
     * mv地址
     */
    MV_PATH("audioCenter/mv"),

    ;

    /**
     * 路径
     */
    private String path;

    PathEnum(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
