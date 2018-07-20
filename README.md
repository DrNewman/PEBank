# PEBank v0.1

Для запуска данной версии приложения необходимо:
1. Склонировать проект из репозитория при помощи IntelliJ IDEA;
2. Создать конфигурацию запуска проекта Spring Boot с указанием com.pebank.demo.DemoApplication в качестве main class;
3. Запустить сборку;
4. Открыть в браузере стартовую страницу http://localhost:8080

Для создания базы данных для приложения есть 2 способа:

1. Быстрый и сугубо временный.
-создать базу командами в консоле MySQL
mysql> create database bank_db;
mysql> create user 'springuser'@'localhost' identified by 'user';
mysql> grant all on bank_db.* to 'springuser'@'localhost';
-запустить проект один раз (в базе автоматически создасться необходимая структура)
-изменить значение параметра spring.jpa.hibernate.ddl-auto в конфигурационном файле приложения application.properties на none (иначе при каждом новом запуске структура будет создаваться заново) 

2. Нормальный
-создать базу командами в консоле MySQL
mysql> create database bank_db;
mysql> create user 'springuser'@'localhost' identified by 'user';
mysql> grant all on bank_db.* to 'springuser'@'localhost';
-создать структуру скриптами (скоро будут добавлены отдельным файлом)
-изменить значение параметра spring.jpa.hibernate.ddl-auto в конфигурационном файле приложения application.properties на none
-запустить приложение
