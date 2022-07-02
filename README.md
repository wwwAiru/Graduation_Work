Инвестбанк Агатион
============

Проект <b>Инвестбанк Агатион</b> - это выпускная работа в рамках обучения компании Egar Technology. 
Основные используемые технологии:
1. Java collection framework
2. Java Stream API  
3. Фреймворк Spring(Spring MVC, Spring Boot)
4. Spring Security
5. Валидация средствами spring(jsr 303) 
6. Шаблонизатор Thymeleaf
7. Для фронтэнда Bootstrap v5.2
8. Субд PostgreSql
9. В качестве слоя взаимодействия с БД использован Spring data JPA, Hibernate в качестве реализации.
_________
Перед запуском проекта необходимо создать базу данных c названием bank и запустить SQL скрипт dump-db-bank в директории проекта resources.
_________
На главной странице проекта отображаются актуальные курсы валют. Информация по курсам валют собирается с сайта центрального банка https://www.cbr-xml-daily.ru/daily_json.js обновляется один раз при старте сервера и далее каждые 4 часа(в классе CurrencyService используется Scheduling). В дальнейшем курсы евро и доллара используются при расчёте перевода рублей на счета в соответствующих валютах.
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
<b>3. Открыть счёт(лучше сразу несколько, в разных валютах)</b>

![createAccount](https://user-images.githubusercontent.com/92391770/176995627-578514a8-18b0-4190-84fd-7673332edc0c.jpg)

<br>
<br>
<b>4. Перевести деньги на счёт </b>

![upAccountBalance1](https://user-images.githubusercontent.com/92391770/176995883-2638804f-a97b-499d-9f9f-de8fd72e0ec1.jpg)
![upAccountBalance](https://user-images.githubusercontent.com/92391770/176995907-b220210f-0fad-4df3-9dd7-518048e8eb2d.jpg)

<br>
<br>
<b>5. Перейти на станицу "Инвестиционные продукты" и выбрать продукты </b>

![investment1](https://user-images.githubusercontent.com/92391770/176996473-c16b83d7-952d-437d-9731-ad70d9960f49.jpg)
![investment](https://user-images.githubusercontent.com/92391770/176996478-2a657792-836d-47d1-899a-cf872d123e13.jpg)

<br>
<br>
<b>6. В личном кабинете отобразятся все вклады с привязкой к счёту, с которого была инвестиция. Если закрыть вклад раньше срока, то денежные средства зачислятся на баланс счёта, а проценты зачислены не будут. </b>

![investment_lk](https://user-images.githubusercontent.com/92391770/177009677-e273ea4a-1671-4972-84eb-e51c4170c4a8.jpg)

<br>
<br>
<b>7. В проекте реализована ролевая система. Существует две роли USER - все клиенты, HEAD_MANAGER - администратор с дополнительными правами. Для того чтобы протестировать закрытие вкладов с процентами, можно зайти в учётную запись администратора и на странице "Инвестиции клиентов" активировать режим закрытия вкладов с процентами. Данные для входа в учётную запись администратора ЛОГИН: admin@gmail.com  ПАРОЛЬ: 11111111 </b>

![admin](https://user-images.githubusercontent.com/92391770/177012289-c76cabd9-4f56-4f0c-b440-95aeaeef2f68.jpg)


<br>
<br>
<b>8. Режим закрытия вкладов с процентами устанавливает даты открытия и окончания вкладов на -11 лет. После этого, зайдя обратно в свою учётную запись, вклады можно закрывать и денежные средства будут зачислены на счёт с процентами. После этого деньги можно вывести на основной баланс, а после - на банковскую карту. </b>
<br>
<br>
<b>9. Пополнение основного баланса и вывод средств на карту пишутся в историю транзакций, где можно посмотреть некоторые детали. </b>

![client_history](https://user-images.githubusercontent.com/92391770/177011018-62c36af7-9589-4402-ad4a-f44a9570f4d1.jpg)


<br>
<br>
<b>10. У администратора(HEAD_MANAGER) реализован функционал: добавление инвестиционных продуктов(вкладов), редактирование, деактивация, удаление. Администратор может назначать роли клиентам на странице "Клиенты". На странице "Деактивированные продукты" администратор может активировать архивный вклад в режиме редавтирования, после этого он будет отображаться на странице "Инвестиционные продукты"), либо удалить безвозвратно.  </b>

![admin_prod_edit](https://user-images.githubusercontent.com/92391770/177010224-ac680090-b6cb-4ee0-8da1-0f6bf520f708.jpg)
![admin_client_edit](https://user-images.githubusercontent.com/92391770/177010297-f8475439-1e5d-469b-9d06-e25c977863ea.jpg)

_________
В проекте реализовано несколько эндпоинтов спецификации REST, HTTP запросы GET POST PUT DELETE. Эндпоинты протестированы в Postman.
1. http://localhost/api/v1/product/all  - GET запрос на получение списка всех инвестиционных продуктов. 
2. http://localhost/api/v1/product/1  - GET запрос на получение инвестиционного продукта по id.
3. http://localhost/api/v1/product/add - POST запрос для добавления нового инвестиционного продукта.
5. http://localhost/api/v1/product/1  - PUT запрос для изменения информации по id инвестиционного продукта.
6. http://localhost/api/v1/product/25  - DELETE запрос для удаления инвестиционного продукта. 

<p>
  Чтобы тело запроса не набирать руками можно скопировать тут.<br>
  {<br>
    "name": "\"Тестовый вклад add by POST request\"",<br>
    "description": "Тестовый вклад для проверки различных функций и валидации",<br>
    "currency": "RUB",<br>
    "minDeposit": 40000.00,<br>
    "maxDeposit": 500000.00,<br>
    "interestRate": 15.00,<br>
    "minDepositTerm": 365,<br>
    "maxDepositTerm": 1095,<br>
    "active": false<br>
}<br>
</p>

__________________
На этом пока всё.
__________________
  










