package com.faker.audioStation.conf;

import cn.hutool.db.Entity;
import cn.hutool.db.handler.EntityListHandler;
import cn.hutool.db.sql.SqlExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
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
    @Autowired
    DataSource dataSource;

    @Autowired
    ResourceLoader resourceLoader;

    @Value("${spring.datasource.url}")
    private String sqliteDbPath;


    /**
     * 初始化数据库
     */
    private void createDatabase() throws IOException {
        String sqlite = sqliteDbPath.substring("jdbc:sqlite::resource:".length(), sqliteDbPath.length());
        //"classpath:templates/1.txt"
        Resource resource = resourceLoader.getResource("classpath:");
        File classpath = resource.getFile();
        File audioStationDbFile = new File(classpath.getAbsolutePath() + File.separator + sqlite);
        log.info("sqlite数据库文件地址:" + audioStationDbFile.getAbsolutePath());
        if (!audioStationDbFile.getParentFile().exists()) {
            audioStationDbFile.getParentFile().mkdirs();
        }
        if (!audioStationDbFile.exists()) {
            audioStationDbFile.createNewFile();
        }
    }

    /**
     * 初始化表
     */
    private void createTables() throws SQLException {
        Connection connection = dataSource.getConnection();
        List<Entity> list = SqlExecutor.query(connection, "SELECT name FROM sqlite_master ", new EntityListHandler());
        List<String> tables = list.stream().map(x -> x.get("name").toString()).collect(Collectors.toList());
        log.info("已存在的数据库:" + tables);
//        if (!tables.contains("device")) {
//            SqlExecutor.execute(connection, "CREATE TABLE \"device\" (\n" +
//                    "  \"id\" integer NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
//                    "  \"ip\" TEXT NOT NULL,\n" +
//                    "  \"port\" TEXT NOT NULL,\n" +
//                    "  \"username\" TEXT NOT NULL,\n" +
//                    "  \"password\" TEXT NOT NULL,\n" +
//                    "  \"serial_number\" text,\n" +
//                    "  \"position\" text,\n" +
//                    "  \"direction\" TEXT\n" +
//                    ");");
//            SqlExecutor.execute(connection, "INSERT INTO \"device\"(\"ip\", \"port\", \"username\", \"password\", \"serial_number\", \"position\", \"direction\") VALUES ('192.168.88.186', '8000', 'admin', 'zj123456', 'G59394420', '安特磁材厂区南门', '进');");
//            SqlExecutor.execute(connection, "INSERT INTO \"device\"(\"ip\", \"port\", \"username\", \"password\", \"serial_number\", \"position\", \"direction\") VALUES ('192.168.88.187', '8000', 'admin', 'zj123456', 'G59394482', '安特磁材厂区南门', '出');");
//
//        }
//        if (!tables.contains("sys_config")) {
//            SqlExecutor.execute(connection, "CREATE TABLE \"sys_config\" ( \"id\" integer NOT NULL PRIMARY KEY AUTOINCREMENT, \"business_key\" text ( 255 ) NOT NULL, \"business_value\" text NOT NULL, \"business_description\" text NOT NULL );\n" +
//                    "CREATE UNIQUE INDEX \"sys_config_business_key\" ON \"sys_config\" ( \"business_key\" ASC );");
//            //初始化参数：
//            SqlExecutor.execute(connection, "INSERT INTO \"sys_config\"( \"business_key\", \"business_value\", \"business_description\") VALUES ( 'AlarmPushAddress', 'http://atcc-workshoptest.ciih.net/java/auth/login', '收到报警后回调外网地址');");
//            SqlExecutor.execute(connection, "INSERT INTO \"sys_config\"( \"business_key\", \"business_value\", \"business_description\") VALUES ( 'AlarmPushAddressToken', 'Authorization=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJsb2dpblR5cGUiOiJ1c2VyIiwibG9naW5JZCI6IjE1MjY3Mzk4MTMxMTk2NiIsImRldmljZSI6ImRlZmF1bHQtZGV2aWNlIiwiZWZmIjoxNjYyNzk5MDE3OTYxfQ.xPQNsuAfV_g7t5XoQ01MNaF44TQYUSP8WbMuApenDQQ', '访问凭证');");
//            SqlExecutor.execute(connection, "INSERT INTO \"sys_config\"( \"business_key\", \"business_value\", \"business_description\") VALUES ( 'AlarmLoginAddress', 'http://atcc-workshoptest.ciih.net/java/auth/getToken', '获取访问凭证地址');");
//            SqlExecutor.execute(connection, "INSERT INTO \"sys_config\"( \"business_key\", \"business_value\", \"business_description\") VALUES ( 'AlarmLoginUserName', '15267398131', '账号');");
//            SqlExecutor.execute(connection, "INSERT INTO \"sys_config\"( \"business_key\", \"business_value\", \"business_description\") VALUES ( 'AlarmLoginPassword', 'admin@123', '密码');");
//        }
    }

    @PostConstruct
    public void init() throws SQLException, IOException {
        //初始化数据库
        createDatabase();
        //初始化数据库表
        createTables();
    }
}
