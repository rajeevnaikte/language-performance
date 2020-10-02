FROM openjdk:15
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
# docker build --no-cache --build-arg JAR_FILE="build/libs/*.jar" -t rajeevnaikte/lp-java .
