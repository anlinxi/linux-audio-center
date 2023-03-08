package com.faker.audioStation.model.vo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@ApiModel("jeesite的分页对象")
public class JeesitePage<T> implements Serializable {
    private int bothNum;
    private String orderBy;
    private long count;
    private int next;
    private int centerNum;
    private Map<String, Object> otherData;
    private int last;
    public static final int COUNT_ONLY_COUNT = -2;
    private String funcName;
    public static final int PAGE_SIZE_NOT_PAGING = -1;
    private static final long serialVersionUID = 1L;
    private int prev;
    public static final int COUNT_NOT_COUNT = -1;
    private long pageSize;
    private int first;
    private int maxPageSize;
    private String funcParam;
    private List<T> list;
    private String pageInfo;
    private long pageNo;

    /**
     * 转换mybatisplus分页对象
     *
     * @param page
     */
    public JeesitePage(Page<T> page) {
        this.list = page.getRecords();
        this.count = page.getTotal();
        this.pageNo = page.getPages();
        this.pageSize = page.getSize();
    }
}
