FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD *-app.jar app.jar
ADD start.sh start.sh
ADD templateFile templateFile
RUN chmod +x start.sh
RUN chmod +rwxX templateFile

RUN apk add -U tzdata
RUN cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
RUN echo "Asia/Shanghai" >  /etc/timezone
RUN apk del tzdata

RUN sh -c 'touch /app.jar'
ENTRYPOINT ["./start.sh"]