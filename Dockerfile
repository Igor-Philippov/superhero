# ===========================================
# Start with a base image containing Java runtime
# FROM openjdk:8-jdk-alpine
#
# Add Maintainer Info
# MAINTAINER Igor Philippov <xxxxx.xxxx@gmail.com>
#
# Add a volume pointing to /tmp
# VOLUME /tmp
#
# Make port 8080 available to the world outside this container
# EXPOSE 8080
#
# The application's jar file
# ARG JAR_FILE=target/shc-0.0.1-SNAPSHOT.jar
#
# Add the application's jar to the container
# ADD ${JAR_FILE} shc.jar
#
# Run the jar file "java","-Djava.security.egd=file:/dev/./urandom","-jar", "/shc.jar"
# ENTRYPOINT ["java", "-jar", "/shc.jar"]
# ===========================================
# Start with a base image containing Java runtime
# FROM openjdk:8-jre
# Add Maintainer Info
# MAINTAINER Igor Philippov <xxxxx.xxxx@gmail.com>
# ARG JAR_FILE
# COPY ./target/${JAR_FILE} /src/main/java/
# WORKDIR /src/main/java/
# Make port 8080 available to the world outside this container
# EXPOSE 8080
# shc-0.0.1-SNAPSHOT.jar	
# CMD ["java", "-jar", "${JAR_FILE}"]
# ======================================================================
FROM openjdk:8-jdk-alpine
MAINTAINER Igor Philippov <xxxxx.xxxx@gmail.com>
VOLUME /tmp
# ARG JAR_FILE
ARG DEPENDENCY=./target/dependency
COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY ${DEPENDENCY}/META-INF /app/META-INF
COPY ${DEPENDENCY}/BOOT-INF/classes /app
# Make port 8080 available to the world outside this container
# EXPOSE 8080
ENTRYPOINT ["java","-cp","app:app/lib/*","com.superhero.SuperHeroApi"]
