package com.inspur.code.generator.serivce;

import cn.hutool.core.io.IoUtil;
import com.alibaba.fastjson.JSON;
import com.inspur.code.generator.config.GeraltorConfig;
import com.inspur.code.generator.domain.TempletItem;
import com.inspur.code.generator.util.StringHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class TempletService {
    @Value("${tiny.code.templetGroup}")
    String codePath;
    @Autowired
    GeraltorConfig geraltorConfig;

    @Value("classpath:${tiny.code.templetGroup}/templetDefine.json")
    private Resource templetJsonResource;

    public List<TempletItem> getTemplets() {
        try {
            String areaData = IoUtil.read(templetJsonResource.getInputStream(), Charset.forName("UTF-8"));
            return JSON.parseArray(areaData, TempletItem.class);
        } catch (IOException e) {
            log.error("", e);
        }
        return null;
    }

    /**
     * 获取模版路径
     *
     * @return
     */
    public String getTempletPath(TempletItem item) {
        String relPath = item.getRelativePath();
        if (relPath != null && !"".equalsIgnoreCase(relPath.trim())) {
            return String.format("/%s/%s/%s", codePath, relPath, item.getName());
        }
        return String.format("/%s/%s", codePath, item.getName());
    }

    public String getOutputFile(TempletItem item, Map<String, Object> map) {
        String outPath = StringHelper.repalceParams(item.getOutPath(), map, null);
        String fileName = StringHelper.repalceParams(item.getOutFileName(), map, null);
        String outputDir = geraltorConfig.getValue("outputDir");
        return String.format("%s/%s/%s", outputDir, outPath, fileName);
    }
}
