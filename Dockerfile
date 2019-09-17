FROM openjdk:8
ADD target/BCHIO.jar BCHIO.jar
VOLUME /data/bchio/
EXPOSE 8086
ENTRYPOINT ["java", "-jar", "BCHIO.jar"]