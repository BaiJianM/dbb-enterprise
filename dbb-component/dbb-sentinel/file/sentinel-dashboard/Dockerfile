FROM openjdk:8
ENV APP_HOME=/apps
WORKDIR $APP_HOME
COPY ./sentinel-dashboard-1.8.3.jar ./sentinel-dashboard.jar
EXPOSE 8858
# Dcsp.sentinel.dashboard.server中的IP为部署sentinel-dashboard的能够访问(外网、局域网)的IP
# project.name 可视化页面中的项目名
ENTRYPOINT ["java","-Xms512m","-Xmx512m", "-Dserver.port=8858", "-Dcsp.sentinel.dashboard.server=172.17.0.1:8858", "-Dproject.name=gientech-sentinel", "-Dsentinel.dashboard.auth.username=admin", "-Dsentinel.dashboard.auth.password=0aDj^JfPuWtzy2Vg","-jar"]
CMD ["sentinel-dashboard.jar"]