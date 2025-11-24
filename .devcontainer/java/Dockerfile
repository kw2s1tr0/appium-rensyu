FROM eclipse-temurin:21-jdk

ENV GRADLE_VERSION=8.6
ENV JAVA_HOME=/opt/java/openjdk
ENV PATH=/opt/gradle-${GRADLE_VERSION}/bin:$PATH

RUN apt update && \
    apt install -y curl unzip && \
    curl -sSL -o gradle.zip https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip && \
    unzip -q gradle.zip -d /opt/ && \
    rm gradle.zip && \
    mkdir /appium