FROM openjdk:8
ENV APP_HOME=/apps
RUN /bin/cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' >/etc/timezone
WORKDIR $APP_HOME
COPY ./gientech-demo-biz-1.0.0.jar ./demo.jar
EXPOSE 7773
ENTRYPOINT ["java","-Xms2048m","-Xmx2048m","-jar"]
CMD ["demo.jar"]