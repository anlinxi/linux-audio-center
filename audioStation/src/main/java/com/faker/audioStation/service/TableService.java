package com.faker.audioStation.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.faker.audioStation.model.dto.GetPageDto;

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
}
