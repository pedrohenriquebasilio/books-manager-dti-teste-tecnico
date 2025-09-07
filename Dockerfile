FROM openjdk:17-jdk-slim
WORKDIR /app
COPY sql/ ./sql/
COPY target/book-manager-1.0.0-jar-with-dependencies.jar .
VOLUME ["/app/data"]
CMD ["java", "-jar", "book-manager-1.0.0-jar-with-dependencies.jar"]
