# Используем базовый образ с OpenJDK 21 и Spring Boot
FROM openjdk:21-jdk-slim

# Устанавливаем рабочую директорию внутри контейнера
WORKDIR /app

# Копируем jar файл микросервиса в контейнер
COPY target/client-management-0.0.1-SNAPSHOT.jar app.jar

# Указываем команду для запуска приложения
ENTRYPOINT ["java","-jar","/app/app.jar"]
