# Используем официальный образ OpenJDK 21
FROM openjdk:21-jdk-slim

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем файлы сборки в контейнер
COPY target/*.jar app.jar
COPY ./beauty_salon/core/target/core-0.0.1-SNAPSHOT.jar core.jar


# Открываем порт
EXPOSE 8084

# Запуск приложения
ENTRYPOINT ["java", "-jar", "app.jar"]