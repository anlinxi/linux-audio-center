package com.faker.audioStation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.faker.audioStation.model.domain.Lyric;
import com.faker.audioStation.model.domain.Mv;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>MV信息mapper</p>
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
 * @date 2023/3/9 11:24
 */
@Mapper
public interface MvMapper extends BaseMapper<Mv> {

}
