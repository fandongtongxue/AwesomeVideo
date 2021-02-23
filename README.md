# AwesomeVideo
基于Spring Boot的《倾心短视频》小程序的**后端API服务**

## 项目功能
+ 注册、登陆账号

+ 发布短视频，并添加背景乐

+ 浏览短视频

+ 收藏短视频

+ 评论、转发短视频

+ 关注用户

## 项目使用的技术

- 框架：Spring Boot 2.0.5.RELEASE、Mybatis

- 数据库：MySQL、Redis

- API文档：Swagger2

- 缓存：Spring Cache

- 插件：Druid连接池、Mybatis Generator、通用Mapper、PageHelper分页插件、Lombok、Spring Boot Devtools热部署


## 配套项目

  本**后端API服务**项目需要与以下两个项目配套使用。

  <a href="https://github.com/lkmc2/AwesomeVideoWxApp">《倾心短视频》微信小程序</a>

  <a href="https://github.com/lkmc2/AwesomeVideoAdmin">后台管理系统</a>

## 项目运行方式
环境
1.安装Redis

brew install redis

//启动
/usr/local/bin/redis-server /usr/local/etc/redis.conf


2.安装FFmpeg

brew install ffmpeg

1. 创建数据库awesome_video。

2. 在数据库中运行src/main/resources下的awesome_video.sql文件。

3. 修改src/main/resources的application.properties配置文件中的的数据库用户名和密码。

4. 运行AwesomeVideoApplication.java启动项目。

5. 浏览器中打开http://localhost:8080/swagger-ui.html ，可访问到项目的API文档。

<img src="https://raw.githubusercontent.com/lkmc2/AwesomeVideo/master/picture/swagger2%E6%88%AA%E5%9B%BE.png"/>
