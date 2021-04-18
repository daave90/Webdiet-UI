FROM openjdk:11
ARG JAR_FILE=target/webdiet-ui-2.0.jar
COPY ${JAR_FILE} webdiet-ui.jar
ENTRYPOINT ["java","-jar","/webdiet-ui.jar"]