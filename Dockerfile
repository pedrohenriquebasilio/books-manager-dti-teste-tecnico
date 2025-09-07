FROM openjdk:17-jdk-slim
RUN apt-get update && apt-get install -y git && rm -rf /var/lib/apt/lists/*
RUN echo $(date)
WORKDIR /app
RUN git clone https://github.com/pedrohenriquebasilio/books-manager-dti-teste-tecnico.git .


VOLUME ["/app/data"]
CMD ["java", "-jar", "target/book-manager-1.0.0-jar-with-dependencies.jar"]
