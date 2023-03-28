package com.faker.audioStation.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.faker.audioStation.model.domain.Lyric;
import com.faker.audioStation.model.domain.Music;
import com.faker.audioStation.model.domain.MusicCover;
import com.faker.audioStation.model.domain.Singer;
import com.faker.audioStation.model.dto.GetMusicPageParamDto;
import com.faker.audioStation.model.dto.IdDto;
import com.faker.audioStation.model.dto.wyy.songDetail.Al;
import com.faker.audioStation.model.dto.wyy.songDetail.SongDetailRootBean;
import com.faker.audioStation.model.dto.wyy.songDetail.Songs;
import com.faker.audioStation.model.dto.wyy.songUrl.SongUrlRootBean;
import com.faker.audioStation.model.vo.LayuiColVo;
import com.faker.audioStation.wrapper.Wrapper;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>DownloadService</p>
 *
 * <p>项目名称：linux-audio-center</p>
 *
 * <p>注释:无</p>
 *
 * <p>Copyright: Copyright Faker(c) 2023/3/28</p>
 *
 * <p>公司: Faker</p>
 *
 * @author 淡梦如烟
 * @version 1.0
 * @date 2023/3/28 14:19
 */
public interface DownloadService  extends IService<Music> {


    /**
     * 下载网易云音乐的歌曲到本地
     *
     * @param songUrlRootBean
     * @return
     */
    SongUrlRootBean downLoadMusic(SongUrlRootBean songUrlRootBean);

    /**
     * 网易云音乐详情
     *
     * @param ids
     * @return
     */
    SongDetailRootBean songDetail(String[] ids);

    /**
     * 保存在线音乐到本地
     *
     * @param songUrlRootBean
     * @param songs
     * @param songJson
     */
    Music saveMusicByWyy(SongUrlRootBean songUrlRootBean, Songs songs, SongDetailRootBean songJson);

    /**
     * 通过网易云id获取歌手信息
     *
     * @param artistIdWyy
     * @return
     */
    Singer getSingerByWyyId(Integer artistIdWyy);

    /**
     * 通过网易云id获取专辑信息
     *
     * @param albumWyy
     * @param music
     * @return
     */
    MusicCover getMusicCoverByWyyId(Al albumWyy, Music music);

    /**
     * 通过网易云id获取歌词信息
     *
     * @param id
     * @param music
     * @return
     */
    Lyric getLyricByWyyId(Long id, Music music);

}
