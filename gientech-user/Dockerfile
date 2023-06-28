FROM openjdk:8
ENV APP_HOME=/apps
RUN /bin/cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' >/etc/timezone
WORKDIR $APP_HOME
COPY ./gientech-user-biz-1.0.0.jar ./user.jar
COPY ./skywalking-agent.jar ./skywalking-agent.jar
EXPOSE 7772
ENTRYPOINT ["java", "-Xms1024m", "-Xmx1024m", "-javaagent:skywalking-agent.jar", "-Dskywalking.agent.service_name=gientech-user", "-Dskywalking.collector.backend_service=skywalking-oap:11800", "-jar"]
CMD ["user.jar"]