FROM frolvlad/alpine-java:jdk8-slim
ENV TZ "Asia/Shanghai"
ENV LANG C.UTF-8
WORKDIR /app
ADD target/havefuncenter-0.0.1-SNAPSHOT.jar /app/prod.jar
RUN chmod -R 755 /app/*
WORKDIR /app
ENTRYPOINT ["java","-jar","prod.jar","--spring.profiles.active=docker"]
EXPOSE 9300