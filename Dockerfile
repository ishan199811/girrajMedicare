FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

# copy ONLY correct folder where pom.xml exists
COPY girrajmedico/girrajmedico/girrajMedicare/girrajmedico/ .

RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
