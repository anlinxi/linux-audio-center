FROM frolvlad/alpine-java:jdk8-slim
MAINTAINER anlinxi@gmail.com
ENV TZ "Asia/Shanghai"
ENV LANG C.UTF-8
WORKDIR /app
ADD target/audioStation-0.0.1-SNAPSHOT.jar /app/music.jar
RUN chmod -R 755 /app/*
WORKDIR /app
ENTRYPOINT ["java","-jar","music.jar","--spring.profiles.active=docker"]
EXPOSE 9306