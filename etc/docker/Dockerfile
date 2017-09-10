FROM frolvlad/alpine-oraclejdk8:slim
MAINTAINER 2GIS World Developers <2gisworld.dev@2Gis.ru>

ADD *.jar app.jar

RUN apk update && \
    apk add libstdc++ && \
    rm -rf /var/cache/apk/* && \
    sh -c 'touch /app.jar'

ENV JAVA_OPTS="-Xmx3000m"

EXPOSE 8080 8080

ENTRYPOINT exec java $JAVA_OPTS -jar /app.jar