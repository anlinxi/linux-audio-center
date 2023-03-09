package com.faker.audioStation.conf;

import com.faker.audioStation.enums.PathEnum;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class FileConfig implements WebMvcConfigurer {

    @Value("${faker.resources:/music/}")
    @ApiModelProperty("资源文件路径")
    private String resourcePath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        //所有img/singerPic/**开头的请求 都会去后面配置的路径下查找资源
        //定位歌手头像地址
        registry.addResourceHandler("/" + PathEnum.SINGER_COVER + "/**").addResourceLocations(
                "file:" + resourcePath + System.getProperty("file.separator") + "img"
                        + System.getProperty("file.separator") + "singerPic" + System.getProperty("file.separator")
        );
        //歌单图片地址
        registry.addResourceHandler("/" + PathEnum.PLAYLIST_COVER + "/**").addResourceLocations(
                "file:" + resourcePath + System.getProperty("file.separator") + "img"
                        + System.getProperty("file.separator") + "songListPic" + System.getProperty("file.separator")
        );
        //歌曲图片地址
        registry.addResourceHandler("/" + PathEnum.PLAYLIST_COVER + "/**").addResourceLocations(
                "file:" + resourcePath + System.getProperty("file.separator") + "img"
                        + System.getProperty("file.separator") + "songPic" + System.getProperty("file.separator")
        );
        //歌曲地址
        registry.addResourceHandler("/" + PathEnum.MUSIC_PATH + "/song/**").addResourceLocations(
                "file:" + resourcePath + System.getProperty("file.separator") + "song" + System.getProperty("file.separator")
        );
        //前端用户头像地址
        registry.addResourceHandler("/img/userPic/**").addResourceLocations(
                "file:" + resourcePath + System.getProperty("file.separator") + "img"
                        + System.getProperty("file.separator") + "userPic" + System.getProperty("file.separator")
        );
        //用户头像默认地址
        registry.addResourceHandler("/img/**").addResourceLocations(
                "file:" + resourcePath + System.getProperty("file.separator") + "img" + System.getProperty("file.separator")
        );
    }

}
