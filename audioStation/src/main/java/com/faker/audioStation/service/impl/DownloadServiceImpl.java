package com.faker.audioStation.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.faker.audioStation.enums.PathEnum;
import com.faker.audioStation.mapper.*;
import com.faker.audioStation.model.domain.Lyric;
import com.faker.audioStation.model.domain.Music;
import com.faker.audioStation.model.domain.MusicCover;
import com.faker.audioStation.model.domain.Singer;
import com.faker.audioStation.model.dto.wyy.songDetail.Al;
import com.faker.audioStation.model.dto.wyy.songDetail.SongDetailRootBean;
import com.faker.audioStation.model.dto.wyy.songDetail.Songs;
import com.faker.audioStation.model.dto.wyy.songUrl.JsonData;
import com.faker.audioStation.model.dto.wyy.songUrl.SongUrlRootBean;
import com.faker.audioStation.scanner.Scanner;
import com.faker.audioStation.service.CacheService;
import com.faker.audioStation.service.DownloadService;
import com.faker.audioStation.util.ToolsUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import top.yumbo.util.music.musicImpl.netease.NeteaseCloudMusicInfo;

import javax.imageio.ImageIO;
import java.io.*;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 * 音乐 服务实现类
 * </p>
 *
 * @author anlin
 * @since 2022-07-26
 */
@Slf4j
@Service
public class DownloadServiceImpl extends ServiceImpl<MusicMapper, Music> implements DownloadService {

    @Value("${faker.resources:/music/}")
    @ApiModelProperty("资源文件路径")
    private String resourcePath;

    @Autowired
    @ApiModelProperty("缓存服务")
    CacheService cacheService;

    @ApiModelProperty("网易云音乐api")
    NeteaseCloudMusicInfo neteaseCloudMusicInfo = new NeteaseCloudMusicInfo();

    @Autowired
    @ApiModelProperty("扫描器")
    Scanner scanner;

    @Autowired
    @ApiModelProperty("音乐封面图片文件Mapper")
    MusicCoverMapper musicCoverMapper;

    @Autowired
    @ApiModelProperty("歌手Mapper")
    SingerMapper singerMapper;

    @Autowired
    @ApiModelProperty("歌词Mapper")
    LyricMapper lyricMapper;

    @Autowired
    @ApiModelProperty("Mv信息mapper")
    protected MvMapper mvMapper;


    @ApiModelProperty(value = "歌曲封面图片类型", notes = "bmp|gif|jpg|jpeg|png")
    private String formatName = "png";

    /**
     * 下载网易云音乐的歌曲到本地
     *
     * @param songUrlRootBean
     * @return
     */
    @Override
    public SongUrlRootBean downLoadMusic(SongUrlRootBean songUrlRootBean) {
        if (null != songUrlRootBean && songUrlRootBean.getData().size() > 0) {
            JsonData jsonData = songUrlRootBean.getData().get(0);
            long wyyId = jsonData.getId();
            String key = "downLoadMusic:" + wyyId;
            try {
                //缓存锁
                if (cacheService.get(key) != null) {
                    return songUrlRootBean;
                }
                cacheService.set(key, new Date(), 5, TimeUnit.MINUTES);
                QueryWrapper<Music> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("WYY_ID", wyyId);
                Music music = this.getOne(queryWrapper);
                if (null != music) {
                    songUrlRootBean.getData().get(0).setUrl("/api/music/getMusic?id=" + music.getId());
                    return songUrlRootBean;
                }
                SongDetailRootBean songJson = this.songDetail(new String[]{wyyId + ""});
                if (null != jsonData.getUrl() && songJson.getSongs().size() > 0) {
                    Songs songs = songJson.getSongs().get(0);
                    music = this.saveMusicByWyy(songUrlRootBean, songs, songJson);
                    songUrlRootBean.getData().get(0).setUrl("/api/music/getMusic?id=" + music.getId());
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                cacheService.delete(key);
            }
        }
        return songUrlRootBean;
    }

    /**
     * 网易云音乐详情
     *
     * @param idsArr
     * @return
     */
    @Override
    public SongDetailRootBean songDetail(String[] idsArr) {
        String ids = Arrays.stream(idsArr).collect(Collectors.joining(","));
        SongDetailRootBean songDetailRootBean = cacheService.get(ids);
        if (songDetailRootBean != null) {
            return songDetailRootBean;
        }
        JSONObject parameter = new JSONObject();
        parameter.put("ids", ids);
        String songJson = neteaseCloudMusicInfo.songDetail(parameter).toJSONString();
        log.info(songJson);
        songDetailRootBean = JSONObject.parseObject(songJson, SongDetailRootBean.class);
        cacheService.set(ids, songDetailRootBean, 30, TimeUnit.DAYS);
        return songDetailRootBean;
    }

    /**
     * 保存在线音乐到本地
     *
     * @param songUrlRootBean
     * @param songs
     * @param songJson
     */
    @Override
    public Music saveMusicByWyy(SongUrlRootBean songUrlRootBean, Songs songs, SongDetailRootBean songJson) {
        Music music = new Music();
        String musicName = songs.getName();
        String artist = "佚名";
        Integer artistIdWyy = null;
        if (songs.getAr().size() > 0) {
            artist = songs.getAr().get(0).getName();
            artistIdWyy = songs.getAr().get(0).getId();
        }
        String type = "mp3";
        try {
            type = songUrlRootBean.getData().get(0).getType();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String musicPath = resourcePath + PathEnum.DOWNLOAD_MUSIC_PATH.getPath() + "/" + ToolsUtil.getFileName(artist + " - " + musicName + "." + type);
        JsonData jsonData = songUrlRootBean.getData().get(0);
        String url = jsonData.getUrl();
        File audio = new File(musicPath);
        if (!audio.getParentFile().exists()) {
            audio.getParentFile().mkdirs();
        }
        boolean isProxy = false;

        if (!isProxy) {
            HttpUtil.downloadFile(url, audio);
        }

        String sha256 = SecureUtil.sha256(new File(audio.getAbsolutePath()));
        //481,115 小文件试听音乐移入缓存
        if (audio.length() == 481115) {
            log.info("小文件试听音乐移入缓存:" + audio.length());
            String tmp = resourcePath + PathEnum.DOWNLOAD_MUSIC_PATH.getPath() + "/tmp/" + ToolsUtil.getFileName(artist + " - " + musicName + "." + type);
            FileUtil.copy(musicPath, tmp, false);
            musicPath = tmp;
            audio.delete();
            music.setStateCode("7");
        }

        music.setHashCode(sha256);
        music.setPath(new File(musicPath).getAbsolutePath());
        music.setTitle(musicName);
        music.setArtist(artist);
        if (null != artistIdWyy && artistIdWyy != 0) {
            //获取歌手信息
            Singer singer = this.getSingerByWyyId(artistIdWyy);
            music.setArtistId(singer.getId());
        }
        String albumPicUrl = null;
        String albumWyy = null;
        Long albumIdWyy = null;
        if (songs.getAl() != null) {
            albumWyy = songs.getAl().getName();
            albumIdWyy = songs.getAl().getId();
            albumPicUrl = songs.getAl().getPicUrl();
        }
        if (null != albumPicUrl && null != albumWyy) {
            //获取专辑封面
            MusicCover musicCover = this.getMusicCoverByWyyId(songs.getAl(), music);
            music.setCoverId(musicCover.getId());
        }

        //获取歌词信息
        Lyric lyric = this.getLyricByWyyId(songs.getId(), music);
        if (null != lyric) {
            music.setLyricId(lyric.getId());
        }

        music.setAlbum(albumWyy);
        music.setAlbumId(albumIdWyy);
//        music.setRate();
//        music.setSongLength();
//        music.setFormat();
        music.setTrack(songs.getCd());
//        music.setYears();
//        music.setType();
//        music.setNote();
//        music.setLanguage();
        music.setCreateTime(new Date());
        music.setWyyId(songs.getId());
        this.save(music);
        return music;

    }

    /**
     * 通过网易云id获取歌手信息
     *
     * @param artistIdWyy
     * @return
     */
    @Override
    public Singer getSingerByWyyId(Integer artistIdWyy) {
        QueryWrapper<Singer> query = new QueryWrapper<>();
        query.eq("WYY_ID", artistIdWyy).or().eq("ID", artistIdWyy);
        Singer singer = singerMapper.selectOne(query);
        if (null == singer) {
            JSONObject searchartist = new JSONObject();
            searchartist.put("id", artistIdWyy + "");
            JSONObject artistJson = neteaseCloudMusicInfo.artistDetail(searchartist);
            JSONObject artistJson2 = artistJson.getJSONObject("data").getJSONObject("artist");
            String coverUrl = artistJson2.getString("cover");

            //歌手名称
            String name = artistJson2.getString("name");
            //歌手简介
            String briefDesc = artistJson2.getString("briefDesc");
            //音乐数量
            Long musicSize = artistJson2.getLong("musicSize");
            //专辑数量
            Long albumSize = artistJson2.getLong("albumSize");
            //网易云音乐id
            Long wyyId = artistJson2.getLong("id");
            singer = new Singer();
            singer.setName(name);
            String artistsCoverPath = resourcePath + PathEnum.SINGER_COVER.getPath() + "/" + ToolsUtil.getFileName(name + "—" + wyyId) + "." + formatName;
            try {
                File dir = new File(artistsCoverPath);
                if (!dir.getParentFile().exists()) {
                    dir.getParentFile().mkdirs();
                }
                ImageIO.write(ImageIO.read(new URL(coverUrl)), formatName, new File(artistsCoverPath));
                singer.setPic(artistsCoverPath);
            } catch (Exception e) {
                e.printStackTrace();
                singer.setPic(coverUrl);
            }
            singer.setIntroduction(briefDesc);
            singer.setMusicSize(musicSize);
            singer.setAlbumSize(albumSize);
            singer.setWyyId(wyyId);
            singerMapper.insert(singer);
        }
        return singer;
    }

    /**
     * 通过网易云id获取专辑信息
     *
     * @param albumWyy
     * @param music
     * @return
     */
    @Override
    public MusicCover getMusicCoverByWyyId(Al albumWyy, Music music) {
        String coverPath = resourcePath + PathEnum.MUSIC_COVER.getPath() + "/" + ToolsUtil.getFileName(music) + "." + formatName;
        QueryWrapper<MusicCover> query = new QueryWrapper<>();
        query.eq("WYY_ID", albumWyy.getId());
        MusicCover musicCover = musicCoverMapper.selectOne(query);
        if (null == musicCover) {
            try {
                File dir = new File(coverPath);
                if (!dir.getParentFile().exists()) {
                    dir.getParentFile().mkdirs();
                }
                musicCover = new MusicCover();
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                ImageIO.write(ImageIO.read(new URL(albumWyy.getPicUrl())), formatName, os);
                InputStream inputStream = new ByteArrayInputStream(os.toByteArray());
                String sha256MusicCover = SecureUtil.sha256(inputStream);
                ImageIO.write(ImageIO.read(new URL(albumWyy.getPicUrl())), formatName, new File(coverPath));
                musicCover.setHashCode(sha256MusicCover);
                musicCover.setPath(coverPath);
                musicCover.setName(music.getTitle());
                musicCover.setWyyId(albumWyy.getId());
                musicCoverMapper.insert(musicCover);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return musicCover;
    }

    /**
     * 通过网易云id获取歌词信息
     *
     * @param id
     * @param music
     * @return
     */
    @Override
    public Lyric getLyricByWyyId(Long id, Music music) {
        QueryWrapper<Lyric> query = new QueryWrapper<>();
        query.eq("WYY_ID", id);
        Lyric lyric = lyricMapper.selectOne(query);
        if (null == lyric) {
            try {
                JSONObject searchlyric = new JSONObject();
                searchlyric.put("id", id + "");
                JSONObject searchlyricResult = neteaseCloudMusicInfo.lyric(searchlyric);
                String lyricText = searchlyricResult.getJSONObject("lrc").getString("lyric");
                String lyricPath = resourcePath + PathEnum.LYRIC_PATH.getPath() + "/" + ToolsUtil.getFileName(music) + ".lrc";
                File lyricPathFile = new File(lyricPath);
                if (!lyricPathFile.getParentFile().exists()) {
                    lyricPathFile.getParentFile().mkdirs();
                }
                FileWriter writer = new FileWriter(lyricPathFile);
                writer.write(lyricText);
                writer.flush();
                writer.close();
                lyric = new Lyric();
                lyric.setId(music.getId());
                lyric.setPath(lyricPath);
                lyric.setName(music.getTitle());
                lyric.setWyyId(searchlyricResult.getJSONObject("lrc").getLong("id"));
                lyricMapper.insert(lyric);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return lyric;
    }
}
