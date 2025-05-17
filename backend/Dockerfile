FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app
COPY backend/mvnw ./mvnw
COPY backend/.mvn ./.mvn
COPY backend/pom.xml ./pom.xml
COPY backend/src ./src
RUN chmod +x mvnw && ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
