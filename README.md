# SkyPaws Mobile app

## Project Description
В проекте используются следующие библиотеки:
* Jetpack Compose
* Room
* OkHttp + Retrofit
* Hilt для DI
* Junit(4) и mockk

Настроен gitlab CI для создания apk артефакта с использованием gitlab secure files и с отправкой файла apk и данных по версии приложения на сервер (для ручного обновления приложения пользователем).
<img src="images/CI.png">
<img src="images/CI_assemble.png"> 

## App Description
### Authentication & Authorization
Аутентификация и авторизация возможны только для работников авиакомпании.  
При различных ошибках (ошибки сервера, отсуствие интернета или неверные данные для входа) отображается уведомление
 <div style="display: flex; margin-bottom: 40px;">
  <img src="images/enter.jpg" style="width: 30%; margin-right: 10px;">
  <img src="images/enter_with_code_error.jpg" style="width: 30%;">
</div>  

При успешной аутентификации загружаются данные с сервера авиакомпании. 
 <div style="display: flex;  margin-bottom: 40px;">
  <img src="images/enter_with_code_sent.jpg" style="width: 30%; margin-right: 10px;">
  <img src="images/enter_with_code_loading.jpg" style="width: 30%;">
</div> 

Для уже авторизованных ранее пользователей, при открытии приложения выполняется загрузка (обновление) данных с сервера авиакомпании.  
<img src="images/crew_plan_loading.jpg" style="width: 30%;">

### Crew Plan (main page)
Главная страница с текущим планом полетов и других событий.  
Данные загружаются с сервера авиакомпании, хранятся в базе данных приложения.  
Имеется возможность обновления данных через pull to refresh.

 <div style="display: flex;  margin-bottom: 40px;">
  <img src="images/crew_plan.jpg" style="width: 30%; margin-right: 10px;">
  <img src="images/crew_plan_expanded.jpg" style="width: 30%; margin-right: 10px;">
  <img src="images/crew_plan_error.jpg" style="width: 30%;">
</div> 


### Navigation Menu
При наличии обновления приложения (без изменения базы данных), доступно скачивание и установка apk файла новой версии с сервера SkyPaws.

 <div style="display: flex;  margin-bottom: 40px;">
  <img src="images/nav_menu.jpg" style="width: 30%; margin-right: 10px;">
   <img src="images/nav_menu_exit.jpg" style="width: 30%; margin-right: 10px;">
  <img src="images/nav_menu_update_available.jpg"  style="width: 30%;">
</div> 

 <div style="display: flex;  margin-bottom: 40px;">
  <img src="images/nav_menu_loading.jpg" style="width: 30%; margin-right: 10px;">
  <img src="images/nav_menu_downloaded.jpg" style="width: 30%;  margin-right: 10px;">
  <img src="images/nav_menu_update.jpg" style="width: 30%;">
</div> 

### Logbook
История полетов по годам и месяцам.  
 <div style="display: flex;  margin-bottom: 40px;">
  <img src="images/logbook.jpg" style="width: 30%; margin-right: 10px;">
   <img src="images/logbook_expanded.jpg" style="width: 30%;">
</div> 

### Paid Services (стадия разработки)
Разрабатываются планые услуги
* скачивание logbook в различных форматах
* синхронизация с google calendar.

Дизайн, логика и стадия оплаты в разработке.
 <div style="display: flex;  margin-bottom: 40px;">
  <img src="images/paid_services.jpg" style="width: 30%; margin-right: 10px;">
   <img src="images/log_paid_service.jpg" style="width: 30%">
</div> 

Пример страницы скачивания файлов (с выбром нескольких или по одному).
 <div style="display: flex;  margin-bottom: 40px;">
  <img src="images/log.jpg" style="width: 30%; margin-right: 10px;">
   <img src="images/log_chosen.jpg" style="width: 30%;">
</div> 
Дополнительная уточняющая информация.
 <div style="display: flex;  margin-bottom: 40px;">
  <img src="images/log_expanded1.jpg" style="width: 30%; margin-right: 10px;">
   <img src="images/log_expanded2.jpg" style="width: 30%;">
</div> 

### Settings
Доступно:
* изменение темы приложения
* изменение отображения кода аэропорта для полетов в CrewPlan (при изменении настроек и открытии crewPlan новый запрос на сервер с другим параметром для кода).
* выбор папки для сохранения файлов (logbook). По умолчанию (если папка не была выбрана пользователем через настройки) для скачивания открывается системный file manager.

 <div style="display: flex;  margin-bottom: 40px;">
  <img src="images/settings.jpg" style="width: 30%; margin-right: 10px;">
   <img src="images/settings_theme.jpg" style="width: 30%; margin-right: 10px;">
  <img src="images/settings_code.jpg"  style="width: 30%;">
</div> 

### Update
Приложение блокируется для использования при наличии новой версии приложения с обновленной базой данных. Данный подход используется для минимизации кода миграции со старых версий БД на новые.

 <div style="display: flex;  margin-bottom: 40px;">
  <img src="images/crew_plan_newDB.jpg" style="width: 30%; margin-right: 10px;">
   <img src="images/crew_plan_update.jpg" style="width: 30%">
</div> 

## Project status
In progress. Not released yet.
