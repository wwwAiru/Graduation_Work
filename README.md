Инвестбанк Агатион
============

Проект <b>Инвестбанк Агатион</b> - это выпускная работа в рамках обучения компании Egar Technology. 
Основные используемые технологии:
1. Java collection framework
2. Java Stream API  
3. Фреймворк Spring(Spring MVC, Spring Boot) 
4. Шаблонизатор Thymeleaf
5. Для фронтэнда Bootstrap v5.2
6. Spring Security
7. Субд PostgreSql
8. В качестве слоя взаимодействия с БД использован Spring data JPA, Hibernate в качестве реализации.
_________
Перед запуском проекта необходимо зоздать базу данных c названием bank и запустить SQL скрипт dump-db-bank в директории проекта resources
_________
После запуска проекта предлагаю опробовать функционал, для этого проделать следующий путь:<br>
<b>1. Зарегистрироваться и пройти в личный кабинет</b>

![register](https://user-images.githubusercontent.com/92391770/176995171-bf3c76da-03c6-4022-bff5-ad1fc460943c.jpg)
<br>
<br>
<b>2. Пополнить баланс </b>

![upBalance](https://user-images.githubusercontent.com/92391770/176995517-ddd7f369-d8c0-4433-b966-9212f97af2e8.jpg)

<br>
<br>
<b>3. Открыть счёт(лучше сразу несколько в разных валютах)</b>

![createAccount](https://user-images.githubusercontent.com/92391770/176995627-578514a8-18b0-4190-84fd-7673332edc0c.jpg)

<br>
<br>
<b>4. Перевести деньги на счёт </b>

![upAccountBalance1](https://user-images.githubusercontent.com/92391770/176995883-2638804f-a97b-499d-9f9f-de8fd72e0ec1.jpg)
![upAccountBalance](https://user-images.githubusercontent.com/92391770/176995907-b220210f-0fad-4df3-9dd7-518048e8eb2d.jpg)

<br>
<br>
<b>5. Перейти на станицу "Инвестиционные продукты" и выбрать продукт </b>

![investment1](https://user-images.githubusercontent.com/92391770/176996473-c16b83d7-952d-437d-9731-ad70d9960f49.jpg)
![investment](https://user-images.githubusercontent.com/92391770/176996478-2a657792-836d-47d1-899a-cf872d123e13.jpg)

<br>
<br>
<b>6. В личном кабинете отобразятся все вклады с привязкой к счёту, с которого была инвестиция. Если закрыть вклад раньше срока, то денежные средства зачислятся на баланс счёта, а проценты зачислены не будут. </b>

![investment_lk](https://user-images.githubusercontent.com/92391770/177009677-e273ea4a-1671-4972-84eb-e51c4170c4a8.jpg)

<br>
<br>
<b>7. В проекте реализована ролевая система. Существует две роли USER - все клиенты, HEAD_MANAGER - администратор с дополнительными правами. Для того чтобы протестировать закрытие вкладов с процентами, можно зайти в учётную запись администратора и на странице "Инвестиции клиентов" активировать режим закрытия вкладов с процентами. Данные для входа в учётную запись администратора ЛОГИН: admin@gmail.com  ПАРОЛЬ: 11111111 </b>

![admin](https://user-images.githubusercontent.com/92391770/177009892-100d1482-5e11-4b59-be65-f43c2f46af66.jpg)







