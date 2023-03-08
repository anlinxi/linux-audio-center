# springBoot项目公共基础框架

个人springBoot项目公共基础框架，主要集成了代码生成和一些常见的鉴权校验等功能。

## 项目框架

项目使用了springboot+mybatis plus+sqlite作为后端，文档使用swagger和knife4j。

支持WebSocket和smb。前端管理页面暂时使用layui。

## 项目启动方法

本项目采用了sqlite作为数据库，去掉了mysql和redis依赖。后期打包为jar或者docker镜像后。 可直接运行在群辉或者威联通等docker环境或java环境下。

项目启动会自动创建数据库和表名，登录默认是admin，密码666666。

##### 配置文件

项目配置文件地址audioStation/src/main/resources/application.yml

也可以放到jar包同级目录读取

扫描的音乐文件地址是/music/，docker映射到群辉的音乐文件夹下。 也可以在配置文件faker.resources里更改