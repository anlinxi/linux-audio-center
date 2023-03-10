package com.faker.audioStation.conf;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.annotation.TableName;
import com.faker.audioStation.model.domain.JsMobileUser;
import com.faker.audioStation.model.dto.SqliteTableStructureDto;
import com.faker.audioStation.util.MyBatisPlusSuppotSqliteInit;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * <p>SqliteInit</p>
 *
 * <p>项目名称：audioCenter</p>
 *
 * <p>注释:无</p>
 *
 * <p>Copyright: Copyright Faker(c) 2023/3/8</p>
 *
 * <p>公司: Faker</p>
 *
 * @author 淡梦如烟
 * @version 1.0
 * @date 2023/3/8 13:17
 */
@Slf4j
@Component
public class SqliteInit {


    @ApiModelProperty("jdbc")
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @ApiModelProperty("资源加载")
    @Autowired
    ResourceLoader resourceLoader;

    @ApiModelProperty("数据源地址")
    @Value("${spring.datasource.url}")
    private String sqliteDbPath;

    @ApiModelProperty("mybatisPlus 实体类位置")
    private final String DOMAIN_PACKAGE = "com.faker.audioStation.model.domain";

    @ApiModelProperty("扫描到的实体类Map")
    public static ConcurrentHashMap<String,Class> classMap = new ConcurrentHashMap<>();

    /**
     * 初始化数据库
     */
    private void createDatabase() throws IOException {
        String sqlite = sqliteDbPath.substring("jdbc:sqlite:".length(), sqliteDbPath.indexOf("?"));
        File audioStationDbFile = new File(sqlite);
        log.info("sqlite数据库文件地址:" + audioStationDbFile.getAbsolutePath());
        if (!audioStationDbFile.getParentFile().exists()) {
            audioStationDbFile.getParentFile().mkdirs();
        }
        if (!audioStationDbFile.exists()) {
            audioStationDbFile.createNewFile();
        }
    }

    @Test
    public void test() {
        String createTableSql = MyBatisPlusSuppotSqliteInit.getInstance().createTable(JsMobileUser.class);
        log.info(createTableSql);
    }


    /**
     * 初始化表
     */
    private void createTables() throws SQLException {
        List<String> tables = jdbcTemplate.queryForList("SELECT name FROM sqlite_master ", String.class);
        log.info("已存在的数据库:" + tables);
        List<String> domainTableNames = new ArrayList<String>();
        //spring工具类，可以获取指定路径下的全部类
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        try {
            String pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
                    ClassUtils.convertClassNameToResourcePath(DOMAIN_PACKAGE) + "/*.class";
            Resource[] resources = resourcePatternResolver.getResources(pattern);
            //MetadataReader 的工厂类
            MetadataReaderFactory readerfactory = new CachingMetadataReaderFactory(resourcePatternResolver);
            for (Resource resource : resources) {
                //用于读取类信息
                MetadataReader reader = readerfactory.getMetadataReader(resource);
                //扫描到的class
                String classname = reader.getClassMetadata().getClassName();
                Class<?> clazz = Class.forName(classname);
                //判断是否有指定主解
                TableName anno = clazz.getAnnotation(TableName.class);
                if (anno != null) {
                    //将注解中的类型值作为key，对应的类作为 value
                    domainTableNames.add(anno.value());
                    classMap.put(anno.value(), clazz);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
//        log.info("实体类表清单:" + domainTableNames);

        for (String tableName : domainTableNames) {
            if (!tables.contains(tableName)) {
                log.info("数据库[" + tableName + "]不存在，正在创建");
                String createTableSql = MyBatisPlusSuppotSqliteInit.getInstance().createTable(classMap.get(tableName));
                jdbcTemplate.update(createTableSql);
            } else {
                List<Map<String, Object>> sqliteTableMapList = jdbcTemplate.queryForList("PRAGMA  table_info(" + tableName + ")");
                List<SqliteTableStructureDto> sqliteTableStructureDto = new ArrayList<>();
                for (Map<String, Object> map : sqliteTableMapList) {
                    SqliteTableStructureDto dto = new SqliteTableStructureDto();
                    BeanUtil.copyProperties(map, dto);
                    dto.setDfltValue(null != map.get("dflt_value") ? String.valueOf(map.get("dflt_value")) : null);
                    dto.setNotNull("1".equals(map.get("notnull")) ? true : false);
                    dto.setPk("1".equals(map.get("pk")) ? true : false);
                    sqliteTableStructureDto.add(dto);
                }
                sqliteTableMapList = null;
//                log.info(sqliteTableStructureDto.toString());
                List<String> createFieldSqlList = MyBatisPlusSuppotSqliteInit.getInstance().createField(classMap.get(tableName), sqliteTableStructureDto);
//                String createFieldSql = createFieldSqlList.stream().collect(Collectors.joining(";\n"));
//                jdbcTemplate.update(createFieldSql);
                for(String sql:createFieldSqlList){
                    jdbcTemplate.update(sql);
                }
            }
        }
        //初始化参数
        int userCount = jdbcTemplate.queryForObject("SELECT COUNT(1) FROM js_mobile_user", Integer.class);
        if (userCount <= 0) {
            jdbcTemplate.update("INSERT INTO `JS_MOBILE_USER`(`USER_CODE`, `LOGIN_CODE`, `USER_NAME`, `TOKEN`, `PASSWORD`, `EMAIL`, `MOBILE`, `PHONE`, `SEX`, `AVATAR`, `SIGN`, `WX_OPENID`, `MOBILE_IMEI`, `USER_TYPE`, `REF_CODE`, `REF_NAME`, `MGR_TYPE`, `PWD_SECURITY_LEVEL`, `PWD_UPDATE_DATE`, `PWD_UPDATE_RECORD`, `PWD_QUESTION`, `PWD_QUESTION_ANSWER`, `PWD_QUESTION_2`, `PWD_QUESTION_ANSWER_2`, `PWD_QUESTION_3`, `PWD_QUESTION_ANSWER_3`, `PWD_QUEST_UPDATE_DATE`, `LAST_LOGIN_IP`, `LAST_LOGIN_DATE`, `FREEZE_DATE`, `FREEZE_CAUSE`, `USER_WEIGHT`, `STATUS`, `CREATE_BY`, `CREATE_DATE`, `UPDATE_BY`, `UPDATE_DATE`, `REMARKS`, `CORP_CODE`, `CORP_NAME`, `EXTEND_S1`, `EXTEND_S2`, `EXTEND_S3`, `EXTEND_S4`, `EXTEND_S5`, `EXTEND_S6`, `EXTEND_S7`, `EXTEND_S8`, `EXTEND_I1`, `EXTEND_I2`, `EXTEND_I3`, `EXTEND_I4`, `EXTEND_F1`, `EXTEND_F2`, `EXTEND_F3`, `EXTEND_F4`, `EXTEND_D1`, `EXTEND_D2`, `EXTEND_D3`, `EXTEND_D4`, `EXTEND_JSON`) " +
                    " VALUES ('001_8f5770b703f24eff90d514e47a66f8c4', 'admin', '管理员', '', '95F8E9E2B22A01857F068E2CD8B6E521', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'employee', NULL, NULL, '1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '172.17.0.1', '2021-03-23 12:18:22', NULL, NULL, 0, '0', '1185868924', '2020-12-31 14:30:34', 'system', '2021-03-23 12:18:22', NULL, '0', 'audio Station', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);");
        }
    }

    @PostConstruct
    public void init() throws SQLException, IOException {
        //初始化数据库
        createDatabase();
        //初始化数据库表
        createTables();
    }

}
