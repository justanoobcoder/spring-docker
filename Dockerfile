FROM eclipse-temurin:17-jdk-jammy AS resolve-dependencies
WORKDIR /app
COPY .mvn .mvn
COPY pom.xml mvnw ./
RUN ["./mvnw", "dependency:go-offline", "-B"]


FROM resolve-dependencies AS build
WORKDIR /app
COPY src ./src
RUN ["./mvnw", "package", "-DskipTests"]


FROM eclipse-temurin:17-jre-jammy AS run
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]