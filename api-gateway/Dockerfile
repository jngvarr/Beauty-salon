# Используем официальный образ OpenJDK 21
FROM openjdk:21-jdk-slim

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем файлы сборки в контейнер
COPY target/*.jar app.jar

# Открываем порт
EXPOSE 8765

# Запуск приложения
ENTRYPOINT ["java", "-jar", "app.jar"]
