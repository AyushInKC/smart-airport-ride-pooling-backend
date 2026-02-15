FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
WORKDIR /app
COPY mvnw mvnw
COPY .mvn .mvn
COPY pom.xml pom.xml
COPY src src
RUN ./mvnw -B -DskipTests package
EXPOSE 8080
ENTRYPOINT ["./mvnw", "spring-boot:run"]
