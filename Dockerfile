FROM gradle:8.5-jdk17 AS build
WORKDIR /app
COPY --chown=gradle:gradle . .
RUN gradle clean build -x test

FROM openjdk:17-jdk
WORKDIR /app

ENV PORT=${PORT}
COPY --from=build /app/build/libs/*.jar /app/app.jar
EXPOSE ${PORT}
CMD ["java", "-jar", "/app/app.jar"]
