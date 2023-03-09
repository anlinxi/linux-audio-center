package com.faker.audioStation.scanner.music;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.faker.audioStation.enums.PathEnum;
import com.faker.audioStation.mapper.MusicCoverMapper;
import com.faker.audioStation.mapper.MusicMapper;
import com.faker.audioStation.mapper.SingerMapper;
import com.faker.audioStation.model.domain.Music;
import com.faker.audioStation.model.domain.MusicCover;
import com.faker.audioStation.model.domain.Singer;
import com.faker.audioStation.model.dto.AudioScanInfoDto;
import com.faker.audioStation.scanner.Scanner;
import com.faker.audioStation.util.ToolsUtil;
import com.faker.audioStation.websocket.WebsocketHandle;
import com.faker.audioStation.wrapper.WebSocketMessageWrapper;
import com.faker.audioStation.wrapper.WrapMapper;
import com.faker.audioStation.wrapper.Wrapper;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.asf.AsfFileReader;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.flac.FlacFileReader;
import org.jaudiotagger.audio.generic.AudioFileReader;
import org.jaudiotagger.audio.mp3.MP3FileReader;
import org.jaudiotagger.audio.mp4.Mp4FileReader;
import org.jaudiotagger.audio.ogg.OggFileReader;
import org.jaudiotagger.audio.real.RealFileReader;
import org.jaudiotagger.audio.wav.WavFileReader;
import org.jaudiotagger.tag.TagException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import top.yumbo.util.music.MusicEnum;
import top.yumbo.util.music.musicImpl.netease.NeteaseCloudMusicInfo;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.io.*;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 音乐扫描器
 */
@Slf4j
@Component
public class MusicScanner implements Scanner {

    @Value("${faker.resources:/music/}")
    @ApiModelProperty("资源文件路径")
    private String resourcePath;

    @Value("${faker.music163Api:http://yumbo.top:3000}")
    @ApiModelProperty("网易云音乐API地址")
    private String music163Api;

    @Autowired
    @ApiModelProperty("音乐文件Mapper")
    MusicMapper musicMapper;

    @Autowired
    @ApiModelProperty("音乐封面图片文件Mapper")
    MusicCoverMapper musicCoverMapper;

    @Autowired
    @ApiModelProperty("歌手Mapper")
    SingerMapper singerMapper;

    @Autowired
    @ApiModelProperty("websocket连接")
    WebsocketHandle websocketHandle;

    @ApiModelProperty("音乐文件列表")
    private ThreadLocal<List<File>> audioList = ThreadLocal.withInitial(() -> new ArrayList<File>());

    @ApiModelProperty(value = "歌曲封面图片类型", notes = "bmp|gif|jpg|jpeg|png")
    private String formatName = "png";

    /**
     * 初始化信息
     *
     * @throws SQLException
     * @throws IOException
     */
    @PostConstruct
    public void init() throws SQLException, IOException {
        if (ToolsUtil.isNotNull(music163Api)) {
            MusicEnum.setBASE_URL_163Music(music163Api);
        }
    }


    /**
     * 开始扫描音乐
     *
     * @param path 扫描路径
     * @return
     */
    @Override
    public Wrapper startScan(String path) {
        File fileList = new File(path);
        if (fileList.exists()) {
            if (fileList.isFile()) {
                audioList.get().add(fileList);
            } else if (fileList.isDirectory()) {
                this.scanFile(fileList);
            }
        }
        log.info("扫描到的音乐文件数量" + audioList.get().size());
        File musicCoverDir = new File(resourcePath + PathEnum.MUSIC_COVER.getPath() + "/");
        if (!musicCoverDir.exists()) {
            musicCoverDir.mkdirs();
        }
        File singerCoverDir = new File(resourcePath + PathEnum.SINGER_COVER.getPath() + "/");
        if (!singerCoverDir.exists()) {
            singerCoverDir.mkdirs();
        }

        //网易云音乐api
        NeteaseCloudMusicInfo neteaseCloudMusicInfo = new NeteaseCloudMusicInfo();
        //读取mp3信息
//        MediaInfoHandler mediaInfoHandler = new MediaInfoHandler();
        int index = 0;
        List<Music> audioScanInfoDtoList = new ArrayList<>();
        for (File audio : audioList.get()) {
            String sha256 = SecureUtil.sha256(new File(audio.getAbsolutePath()));
            QueryWrapper<Music> musicQueryWrapperCount = new QueryWrapper<Music>();
            musicQueryWrapperCount.eq("HASH_CODE", sha256);
            int count = musicMapper.selectCount(musicQueryWrapperCount);
            if (count > 0) {
                log.info("已存在歌曲[" + audio.getName() + "],sha256[" + sha256 + "]");
                continue;
            }

            websocketHandle.fanoutMessage(WebSocketMessageWrapper.ok("扫描进度", "正在扫描歌曲[" + audio.getName() + "]...", null));
            AudioFileReader audioFileReader = null;
            String audioName = audio.getName().toLowerCase();
            if (audioName.endsWith(".mp3")) {
                audioFileReader = new MP3FileReader();
            } else if (audioName.endsWith(".mp4")) {
                audioFileReader = new Mp4FileReader();
            } else if (audioName.endsWith(".flac")) {
                audioFileReader = new FlacFileReader();
            } else if (audioName.endsWith(".asf")) {
                audioFileReader = new AsfFileReader();
            } else if (audioName.endsWith(".ogg")) {
                audioFileReader = new OggFileReader();
            } else if (audioName.endsWith(".real")) {
                audioFileReader = new RealFileReader();
            } else if (audioName.endsWith(".wav")) {
                audioFileReader = new WavFileReader();
            } else {
                continue;
            }
            try {
                AudioFile audioFile = audioFileReader.read(audio);
                AudioScanInfoDto audioScanInfoDto = new AudioScanInfoDto(audioFile);

                //保存歌曲信息
                Music music = new Music();
                music.setHashCode(sha256);
                BeanUtil.copyProperties(audioScanInfoDto, music);
                music.setCreateTime(new Date());
                audioScanInfoDtoList.add(music);

                //保存图片封面信息
                MusicCover musicCover = null;
                String coverPath = resourcePath + PathEnum.MUSIC_COVER.getPath() + "/" + music.getTitle() + "." + formatName;
                if (null != audioScanInfoDto.getCover()) {
                    ByteArrayOutputStream os = new ByteArrayOutputStream();
                    ImageIO.write(audioScanInfoDto.getCover(), "png", os);
                    InputStream inputStream = new ByteArrayInputStream(os.toByteArray());
                    String sha256MusicCover = SecureUtil.sha256(inputStream);
                    QueryWrapper<MusicCover> musicCoverQueryWrapper = new QueryWrapper<>();
                    musicCoverQueryWrapper.eq("HASH_CODE", sha256MusicCover);
                    musicCover = musicCoverMapper.selectOne(musicCoverQueryWrapper);
                    if (null == musicCover) {
                        ImageIO.write(audioScanInfoDto.getCover(), formatName, new File(coverPath));
                        musicCover = new MusicCover();
                        musicCover.setHashCode(sha256MusicCover);
                        musicCover.setPath(coverPath);
                        musicCover.setName(music.getTitle());
                        musicCoverMapper.insert(musicCover);
                    }
                    music.setCoverId(musicCover.getId());
                }

                //todo 查询下载专辑封面/歌手封面
                JSONObject searJsonObject = new JSONObject();
                searJsonObject.put("keywords", music.getTitle() + "" + music.getAlbum() + " " + music.getArtist());
                searJsonObject.put("type", "1");
                searJsonObject.put("limit", "1");
                JSONObject searJsonResult = neteaseCloudMusicInfo.search(searJsonObject);
                JSONArray musicInfoArray = searJsonResult.getJSONObject("result").getJSONArray("songs");
                if (musicInfoArray.size() > 0) {
                    JSONObject musicJson = musicInfoArray.getJSONObject(0);
                    //专辑信息
                    JSONObject album = musicJson.getJSONObject("album");
                    if (null == musicCover && null != album) {
                        try {
                            JSONObject artist = album.getJSONObject("artist");
                            String img1v1Url = artist.getString("img1v1Url");
                            musicCover = new MusicCover();
                            ByteArrayOutputStream os = new ByteArrayOutputStream();
                            ImageIO.write(ImageIO.read(new URL(img1v1Url)), formatName, os);
                            InputStream inputStream = new ByteArrayInputStream(os.toByteArray());
                            String sha256MusicCover = SecureUtil.sha256(inputStream);
                            ImageIO.write(audioScanInfoDto.getCover(), formatName, new File(coverPath));
                            musicCover.setHashCode(sha256MusicCover);
                            musicCover.setPath(coverPath);
                            musicCover.setName(music.getTitle());
                            musicCoverMapper.insert(musicCover);
                            music.setAlbumId(album.getLong("id"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }


                    //歌手信息
                    JSONArray artists = musicJson.getJSONArray("artists");
                    if (artists.size() > 0) {
                        try {
                            JSONObject artist = artists.getJSONObject(0);
                            Long artistId = artist.getLong("id");
//                        String artistName = artist.getString("name");
//                        String artistCover = artist.getString("img1v1Url");
                            JSONObject searchartist = new JSONObject();
                            searchartist.put("id", artistId + "");
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
                            //归纳歌曲专辑/歌手信息
                            Singer singer = singerMapper.selectById(wyyId);
                            if(null == singer){
                                singer = new Singer();
                                singer.setName(name);
                                String artistsCoverPath = resourcePath + PathEnum.SINGER_COVER.getPath() + "/" + name + "." + formatName;
                                try {
                                    ImageIO.write(ImageIO.read(new URL(coverUrl)), formatName, new File(artistsCoverPath));
                                    singer.setPic(artistsCoverPath);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    singer.setPic(coverUrl);
                                }
                                singer.setIntroduction(briefDesc);
                                singer.setMusicSize(musicSize);
                                singer.setAlbumSize(albumSize);
                                singer.setWyy_id(wyyId);
                                singerMapper.insert(singer);
                            }
                            music.setArtistId(singer.getId());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    try {
                        //查询下载歌曲歌词
                        Long wyyMusicIdLong = musicJson.getLong("id");
                        JSONObject searchlyric = new JSONObject();
                        searchlyric.put("id", wyyMusicIdLong + "");
                        JSONObject searchlyricResult = neteaseCloudMusicInfo.lyric(searchlyric);
                        String lyric = searchlyricResult.getJSONObject("lrc").getString("lyric");
                        music.setLyric(lyric);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        //todo 爬取歌曲热度、播放量、评论等额外信息 还没想好怎么保存
//                        Long wyyMusicIdLong = musicJson.getLong("id");
//                        JSONObject searchComment = new JSONObject();
//                        searchComment.put("id", wyyMusicIdLong + "");
//                        JSONObject searchCommentResult = neteaseCloudMusicInfo.commentMusic(searchComment);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                musicMapper.insert(music);

            } catch (CannotReadException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TagException e) {
                e.printStackTrace();
            } catch (ReadOnlyFileException e) {
                e.printStackTrace();
            } catch (InvalidAudioFrameException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (index++ > 3) {
                break;
            }
        }


        return WrapMapper.ok();
    }

    /**
     * 递归扫描文件
     *
     * @param file
     */
    @Override
    public void scanFile(File file) {
        if (file.exists() && file.isDirectory()) {
            File[] dirs = file.listFiles(new MusicFileFilter());
            for (File child : dirs) {
                if (child.isFile()) {
                    audioList.get().add(child);
                } else if (child.isDirectory()) {
                    this.scanFile(child);
                }
            }
        }
    }
}
