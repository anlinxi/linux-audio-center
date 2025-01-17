package com.faker.audioStation.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.faker.audioStation.model.dto.DeleteDataDto;
import com.faker.audioStation.model.dto.GetPageDto;
import com.faker.audioStation.model.dto.UpdateDataDto;
import com.faker.audioStation.wrapper.Wrapper;

/**
 * <p>
 * 表格 服务类
 * </p>
 *
 * @author anlin
 * @since 2022-07-26
 */
public interface TableService {


    /**
     * 获取实体类文件的分页数据
     *
     * @param pageSizeDto
     * @return
     */
    IPage<?> getPage(GetPageDto pageSizeDto);

    /**
     * 删除一条数据
     *
     * @param param
     * @return
     */
    Wrapper delete(DeleteDataDto param);

    /**
     * 修改一条数据
     *
     * @param param
     * @return
     */
    Wrapper updateData(UpdateDataDto param);
}
