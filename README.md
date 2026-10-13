# Autofizra

Небольшое консольное Java-приложение, разработанное для автоматической записи на занятия по физкультуре в университете ИТМО через сервис my.itmo.ru

## Используемые технологии

- **Java 17**, **Maven**
- [**my-itmo-api**](https://github.com/alllexey-dev/my-itmo-api) - Java-библиотека для работы с API my.itmo.ru, автор — **alllexey-dev**
- [**Lanterna**](https://github.com/mabe02/lanterna) - библиотека для текстового интерфейса в терминале

## Структура проект

```
.
├── LICENSE
├── pom.xml
├── README.md
├── src
│   └── main
│       └── java
│           └── com
│               └── art3m
│                   └── autofizra
│                       ├── AuthService.java
│                       ├── AutoSignUp.java
│                       ├── gui
│                       │   ├── AppWindow.java
│                       │   ├── LessonPanel.java
│                       │   ├── LessonsPanel.java
│                       │   ├── LoginPanel.java
│                       │   ├── SchedulePanel.java
│                       │   └── SportsPanel.java
│                       ├── Main.java
│                       ├── SportService.java
│                       ├── SportType.java
│                       └── WeekDay.java
```


## Сборка проект

Требования: JDK 17+ и Maven.
 
```bash
git clone https://github.com/artem-korhonen/autofizra-itmo.git
cd autofizra-itmo
mvn clean package
java -jar target/autofizra-1.0-SNAPSHOT.jar
```
