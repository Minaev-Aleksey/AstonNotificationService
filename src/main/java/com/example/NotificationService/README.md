ДЗ 5.
Часть1
Реализовать микросервис(notification-service) для отправки сообщения на почту при удалении или добавлении пользователя.

Использовать необходимые модули spring и kafka.
При удалении или создании юзера приложение, реализованное до этого(user-service), должно отправлять сообщение в kafka,
в котором содержится информация об операции(удаление или создание) и email юзера.

Часть2
Новый микросервис(notification-service) должен получить сообщение из kafka и отправить сообщение на почту юзера 
в зависимости от операции: удаление - Здравствуйте! Ваш аккаунт был удалён. Создание - Здравствуйте! 
Ваш аккаунт на сайте ваш сайт был успешно создан.
Также отдельно добавить API, которая будет отправлять сообщение на почту(почти тот же функционал, что и через кафку).
Написать интеграционные тесты для проверки отправки сообщения на почту.