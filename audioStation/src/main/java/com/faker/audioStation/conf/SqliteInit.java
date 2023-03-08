package com.faker.audioStation.conf;

import com.baomidou.mybatisplus.annotation.TableName;
import com.faker.audioStation.model.domain.JsMobileUser;
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
import java.util.List;

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
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
//        log.info("实体类表清单:" + domainTableNames);

        for (String tableName : domainTableNames) {
            if (!tables.contains(tableName)) {
                log.info("数据库[" + tableName + "]不存在，正在创建");
                String createTableSql = MyBatisPlusSuppotSqliteInit.getInstance().createTable(JsMobileUser.class);
                jdbcTemplate.update(createTableSql);
            }
        }
        //初始化参数
        int userCount = jdbcTemplate.queryForObject("SELECT COUNT(1) FROM js_mobile_user", Integer.class);
        if (userCount <= 0) {
            jdbcTemplate.update("INSERT INTO `js_mobile_user`(`user_code`, `login_code`, `user_name`, `token`, `password`, `email`, `mobile`, `phone`, `sex`, `avatar`, `sign`, `wx_openid`, `mobile_imei`, `user_type`, `ref_code`, `ref_name`, `mgr_type`, `pwd_security_level`, `pwd_update_date`, `pwd_update_record`, `pwd_question`, `pwd_question_answer`, `pwd_question_2`, `pwd_question_answer_2`, `pwd_question_3`, `pwd_question_answer_3`, `pwd_quest_update_date`, `last_login_ip`, `last_login_date`, `freeze_date`, `freeze_cause`, `user_weight`, `status`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `corp_code`, `corp_name`, `extend_s1`, `extend_s2`, `extend_s3`, `extend_s4`, `extend_s5`, `extend_s6`, `extend_s7`, `extend_s8`, `extend_i1`, `extend_i2`, `extend_i3`, `extend_i4`, `extend_f1`, `extend_f2`, `extend_f3`, `extend_f4`, `extend_d1`, `extend_d2`, `extend_d3`, `extend_d4`, `extend_json`) " +
                    " VALUES ('001_8f5770b703f24eff90d514e47a66f8c4', 'admin', '管理员', '', '95F8E9E2B22A01857F068E2CD8B6E521', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'employee', NULL, NULL, '0', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '172.17.0.1', '2021-03-23 12:18:22', NULL, NULL, 0, '0', '1185868924', '2020-12-31 14:30:34', 'system', '2021-03-23 12:18:22', NULL, '0', 'audio Station', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);");
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
