# Документация проекта
### СОДЕРЖАНИЕ 
1. [Введение](#1)
2. [Аналоги](#4) <br>
3. [Требования пользователя](#2) <br>
  2.1. [Программные интерфейсы](#2.1) <br>
  2.2. [Интерфейс пользователя](#2.2) <br>
  2.3. [Характеристики пользователей](#2.3) <br>
  2.4. [Предложения и зависимости](#2.4) <br>
4. [Системные требования](#3) <br>
  4.1 [Функциональные требования](#3.1) <br>
  4.2 [Нефункциональные требования](#3.2) <br>
    4.2.1 [Атрибуты качества](#3.2.1) <br>
    4.2.2 [Ограничения](#3.2.2) <br>
 
### 1. Введение <a name="1"></a>
 В последнее время крупные стриминговые платформы ухудшают качество бесплатного пользования своей платформой. И нормально пользоваться платформой можно только приобретя премиум аккаунт. <br>
 Поэтому было принято решение создать telegram-bot'а, который бы позволял людям скачивать видео- или аудиофайлы с YouTube и потреблять контент оффлайн.
 ### 2. Аналоги <a name="2"></a>
  [Converto.io](https://telegram.me/converto_bot) — бот со схожим функционалом. Только здесь отправляются не медиафайлы, а ссылки на их скачивание со стороннего ресурса.
### 3. Требования пользователя <a name="3"></a>
#### 3.1. Программные интерфейсы <a name="3.1"></a>
 Проект использует платформу [youtube-dl](https://youtube-dl.org/) для скачивания видеофайлов с [YouTube](https://www.youtube.com/). И [youtubedl-java API](https://github.com/lepouletsuisse/youtubedl-java) для взаимодействия с платформой youtube-dl. <br>
 Для извлечения аудиодорожки из видеофайла используется библиотека [JAVE (Java Audio Video Encoder)](http://www.sauronsoftware.it/projects/jave/). <br>
 Для взаимодействия с [Telegram](https://telegram.org/) используется [Spring framework](https://spring.io/projects/spring-framework) совместно с [Telegram API](https://core.telegram.org/api).
#### 3.2. Интерфейс пользователя <a name="3.2"></a>
* Пользовательский интерфейс при первом использовании. <br>
    ![img_0](https://github.com/theAngryBeavers/TelegramBot/blob/main/documentation/images/img_0.png)
    <p/>
* Пользовательский интерфейс после нажатия кнопки `start`. <br>
    ![img_5](https://github.com/theAngryBeavers/TelegramBot/blob/main/documentation/images/img_5.png)
    <p/>
* Пользователь отправил сообщение не в формате `mp3/mp4 youtubeLink`. <br>
    ![img_5](https://github.com/theAngryBeavers/TelegramBot/blob/main/documentation/images/img_5.png)
    <p/>
* Пользователь отправил сообщение в формате `mp3/mp4 youtubeLink`. <br>
    ![img_1](https://github.com/theAngryBeavers/TelegramBot/blob/main/documentation/images/img_1.png)
    ![img_2](https://github.com/theAngryBeavers/TelegramBot/blob/main/documentation/images/img_2.png)
    <p/>
* Пользователь отправил сообщение в формате `mp3/mp4 youtubeLink` и ссылка оказалась неверной. <br>
    ![img_3](https://github.com/theAngryBeavers/TelegramBot/blob/main/documentation/images/img_3.png)
    <p/>
* Пользователь отправил новое сообщение в формате `mp3/mp4 youtubeLink` не дождавшись ответа бота. <br>
    ![img_4](https://github.com/theAngryBeavers/TelegramBot/blob/main/documentation/images/img_4.png)
#### 3.3. Характеристики пользователей <a name="3.3"></a>
 Целевая аудитория бота — все люди, которые не готовы платить деньги за столь тривиальные вещи, как скачивание и потребление контента оффлайн.
#### 3.4. Предположения и зависимости <a name="3.4"></a>
 Бот будет достпен всем пользователям, которые смогут на своих устройствах получить доступ к Telegram. Подробнее об устройствах, поддерживаемых Telegram, Вы можете узнать [здесь](https://telegram.org/faq#q-which-devices-can-i-use).
 ### 4. Системные требования <a name="4"></a>
 #### 4.1. Функциональные требования <a name="4.1"></a>
 
 Пользователю предоставлены следующие возможности:
   1. начать диалог с ботом;
   2. общаться с ботом посредством отправки ему сообщений вида `mp3/mp4 youtubeLink`;
   3. получать от бота файлы формата `.mp3` или `.mp4`;
   4. удалить бота.
   
 #### 4.2 Нефункциональные требования <a name="4.2"></a>
 
  #### 4.2.1 Атрибуты качества <a name="4.2.1"></a>
  <a name="requirements_for_ease_of_use"/>
  
  * Системные требования бота полностью зависят от ситемных требований Telegram. Более детальную информацию о системных требования Вы можете узнать [здесь](https://telegram.org/faq).
   
  #### 4.2.2 Ограничения <a name="4.2.2"></a>
  * Ограничения бота полностью зависят от ситемных требований Telegram. Более детальную информацию об ограничениях Вы можете узнать [здесь](https://telegram.org/faq).
  <a name="security_requirements"/>
