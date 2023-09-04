FROM openjdk:8-jdk-alpine
WORKDIR /app
COPY build/libs/nic-online-store-0.1.jar /app/nic-online-store.jar
EXPOSE 8080
CMD ["java", "-jar", "/app/nic-online-store.jar"]