# ====== 第一ステージ ======
FROM maven:3.9.8-eclipse-temurin-21 AS build
WORKDIR /app

#依存関係を先にインストールしてキャッシュ化
COPY pom.xml .
RUN mvn -B -q dependency:go-offline

#ビルド
COPY src ./src
RUN mvn -B clean package -DskipTests

# ====== 第二ステージ ======
FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

#実行
ENTRYPOINT ["java","-jar","app.jar"]
