# springBoot音乐中心

群辉audio station不对外开放，每个版本的api差异都很大，不好做统一开发。 干脆直接自己写个后台管理系统来对接web和app算了，功能也能更加自定义化

项目很多资源都是依赖了开源社区提供的方案，通过这个项目整合到了一起。

## 项目框架

项目使用了springboot+mybatis plus+sqlite作为后端，文档使用swagger和knife4j。

支持WebSocket和smb。前端管理页面暂时使用layui。管理页面地址/table.html

## 项目启动方法

本项目采用了sqlite作为数据库，去掉了mysql和redis依赖。后期打包为jar或者docker镜像后。 可直接运行在群辉或者威联通等docker环境或java环境下。

项目启动会自动创建数据库和表名，登录默认是admin，密码666666。

##### 配置文件

项目配置文件地址audioStation/src/main/resources/application.yml

也可以放到jar包同级目录读取。

扫描的音乐文件地址是/music/，docker映射到群辉的音乐文件夹下。 也可以在配置文件faker.resources里更改

##### 网易云音乐 API
网易云音乐 API 可以用在线的，也可以自己搭设，直接在群辉docker里搜索<font style="color:#FF9900">binaryify/netease_cloud_music_api</font>即可

然后在application.yml的faker.music163Api里改成自己的地址

###### 前端源码
前端使用了[vue_pc_music](https://gitee.com/trtst/vue_pc_music) 项目改造而,

项目基于vue2的开源项目：[改造后的前端项目地址](http://www.anlinxi.top:30000/an/vue_pc_music)

## 开源社区支持

前端界面使用了[vue_pc_music](https://gitee.com/trtst/vue_pc_music) 项目改造而成，替代了网易云爬取的页面

后台调用网易云的内容使用了[网易云音乐api](https://binaryify.github.io/NeteaseCloudMusicApi) 和[java调音乐api](https://github.com/1015770492/yumbo-music-utils)