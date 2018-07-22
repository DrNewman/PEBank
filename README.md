# PEBank v0.2

Для запуска данной версии приложения необходимо:
1. Склонировать проект из репозитория при помощи IntelliJ IDEA;
2. Создать конфигурацию запуска проекта Spring Boot с указанием com.pebank.demo.DemoApplication в качестве main class;
3. Запустить сборку;
4. Открыть в браузере стартовую страницу http://localhost:8080

Для создания базы данных для приложения есть 2 способа:

1. Нормальный
1.1 создать базу командами в консоле MySQL
  mysql> create database bank_db;
  mysql> create user 'springuser'@'localhost' identified by 'user';
  mysql> grant all on bank_db.* to 'springuser'@'localhost';
1.2 создать структуру скриптами (Dump20180721_1.sql)
1.3 запустить приложение

2. Быстрый
2.1 создать базу командами в консоле MySQL
  mysql> create database bank_db;
  mysql> create user 'springuser'@'localhost' identified by 'user';
  mysql> grant all on bank_db.* to 'springuser'@'localhost';
2.2 изменить значение параметра spring.jpa.hibernate.ddl-auto в конфигурационном файле приложения application.properties на create
2.3 запустить проект один раз (в базе автоматически создасться необходимая структура)
2.4 изменить значение параметра spring.jpa.hibernate.ddl-auto в конфигурационном файле приложения application.properties на none (иначе при каждом новом запуске структура будет создаваться заново)

# PEBank v0.1

Для запуска данной версии приложения необходимо:
1. Склонировать проект из репозитория при помощи IntelliJ IDEA;
2. Создать конфигурацию запуска проекта Spring Boot с указанием com.pebank.demo.DemoApplication в качестве main class;
3. Запустить сборку;
4. Открыть в браузере стартовую страницу http://localhost:8080

Для создания базы данных для приложения есть 2 способа:

1. Быстрый и сугубо временный.
1.1 создать базу командами в консоле MySQL
  mysql> create database bank_db;
  mysql> create user 'springuser'@'localhost' identified by 'user';
  mysql> grant all on bank_db.* to 'springuser'@'localhost';
1.2 запустить проект один раз (в базе автоматически создасться необходимая структура)
1.3 изменить значение параметра spring.jpa.hibernate.ddl-auto в конфигурационном файле приложения application.properties на none (иначе при каждом новом запуске структура будет создаваться заново) 

2. Нормальный
2.1 создать базу командами в консоле MySQL
  mysql> create database bank_db;
  mysql> create user 'springuser'@'localhost' identified by 'user';
  mysql> grant all on bank_db.* to 'springuser'@'localhost';
2.2 создать структуру скриптами (Dump20180721.sql)
2.3 изменить значение параметра spring.jpa.hibernate.ddl-auto в конфигурационном файле приложения application.properties на none
2.4 запустить приложение

