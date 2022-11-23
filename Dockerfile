# Equivalent to FROM openjdk:9-jre-slim-sid, Replace with appropriate jdk path
FROM openjdk:11-jre-slim
ARG module_jar_file
WORKDIR /opt
COPY $module_jar_file APP.jar
CMD ["java", "-jar", "APP.jar", "START_SERVER"]
