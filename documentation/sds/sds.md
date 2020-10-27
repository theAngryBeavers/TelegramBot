# SDS

### Содержание
1. [Используемые технологии](#1) <br>
    1.1. [Java 15](#1.1) <br>
    1.2. [Spring Boot 2.2.5.RELEASE](#1.2) <br>
    1.3. [Telegram Bot API 4.9.1](#1.2) <br>
    1.4. [Youtube-dl API 1.0](#1.3) <br>
    1.5. [Sqlite-jdbc API 3.32.3.2](#1.4) <br>
    1.6. [JAVE 2.3.7](#1.5)
2. [API бота](#2)
3. [Структура БД](#3) <br>
    3.1. [ydBotUsers](#3.1) <br>
    3.2. [ydBotUploadedFiles](#3.2)<br>
    3.3. [Пример взаимодействия с БД](#3.3)<br>

### 1. Используемые технологии <a name="1"></a>
#### 1.1. Java 15 <a name="1.1"></a>
Основной язык разработки.

#### 1.2. Spring Boot 2.2.5.RELEASE <a name="1.2"></a>
В проекте используется фреймворк Spring, т.к. он предоставляет удобный и гибкий механизм конфигурирования приложения. Данный фреймворк используется для построения серверной части и бизнес логики приложения.

#### 1.3. Telegram Bot API 4.9.1 <a name="1.3"></a>
Данный фреймворк предоставляет удобный интерфейс для взаимодействия с Telegram API при помощи Java. Удобство этого фреймворка заключается в том, что он инкапсулирует все запросы к Telegram API соответсвующими java-классами.

#### 1.4. Youtube-dl API 1.0 <a name="1.3"></a>
Это API, написанное на Java предоставляет удобный и понятный интерфейс для взяимодействия с приложением youtube-dl, которое позволяет скачивать медиафайлы со стримингового сервиса YouTube. Посредством данного приложения и API, для работы с ним, реализован процес скачивания медиафайлов.

#### 1.5. Sqlite-jdbc API 3.32.3.2 <a name="1.4"></a>
Данный API используется взаимодействия приложения с БД SQLite-3.

#### 1.6. JAVE 2.3.7 <a name="1.5"></a>
Это библиотека для работы с видео- и аудиофайлами. При помощи данной библиотеки происходит конвертирование виодеофайлов в аудиофайлы.

### 2. API бота <a name="2"></a>
Для взаимодействия бота с Telegram, бота нужно зарегестрировать и получить токен, который будет частью `url`, на который бот будет отправлять ответы. Существует два типа реализации ботов в Telegram:
 * Polling bots
 * Webhook bots <br>
 
Первый тип ботов отправляет запрос к Telegram через заданный промежуток времени ***(назовем такой тип ботов активными)***, а второй — получает от Telegram запрос с информацией от пользователей ***(назовоем такой тип ботов пассивными)***.  <br>
Данный бот является пассивным, т. е. Telegram присылает запрос боту в том случае, если к боту обратился пользователь. В свою очередь бот обрабатывает запрос пользователя и отправляет ответ назад. <br>
Вот как выглядит обработчик запросов от Telegram:

```java
@RequestMapping(value = "/", method = RequestMethod.POST)
public BotApiMethod onUpdateReceived(@RequestBody Update update) {
	return ydBot.onWebhookUpdateReceived(update);
}
```
После обработки пользовательского ввода бот может отправить сообщение, видео- или аудиофайл:
 * отправка сообщения
 ```java
 execute(new SendMessage(chatId, infoMessage));
 ```
 * отправка видеофайла
 ```java
var video = new SendVideo();
audio.setVideo(response.getName(), response.getContentStream());
audio.setChatId(chatId);
message = execute(video);
fileId = message.getVideo().getFileId();
 ```
 * отправка аудиофайла
 ```java
var audio = new SendAudio();
audio.setAudio(response.getName(), response.getContentStream());
audio.setChatId(chatId);
message = execute(audio);
fileId = message.getAudio().getFileId();
 ```

### 3. Структура БД <a name="3"></a>
В проекте используется реляционная легковесная база данных SQLite-3. Для взаимодействия с БД используется драйвер JDBC SQLite. <br>
База данных используется в проекте для сохранения состояния бота для каждого пользователя и для сохранения уникальных идентификаторов загруженных файлов, чтобы не выполнять скачивания и конвертирования, если это необходимо, лишний раз, а сразу отправлять файл через этот уникальный идентификатор.
База данных содержит в себе две таблицы: 

#### 3.1. ydBotUsers <a name="3.1"></a>
![img](https://github.com/theAngryBeavers/TelegramBot/blob/main/documentation/images/table_1.png) <br>
Первичным кючем таблицы ***`ydBotUsers`*** является столбец `chatId`.<br>
В столбце `botState` хранится состояние бота (`botState ∈ { "0 -> READY", "1 -> BUSY" }`).

#### 3.2. ydBotUploadedFiles <a name="3.2"></a>
![img](https://github.com/theAngryBeavers/TelegramBot/blob/main/documentation/images/table_2.png) <br>
Первичным ключем таблицы ***`ydBotUploadedFiles`*** является столбецы `videoId` и `mediaType`, <br>
где `videoId` — YouTube ID медиафайла, полученное из ссылки, и `mediaType` — тип медиафайла (`mediaType ∈ { "0 -> AUDIO", "1 -> VIDEO" }`). <br>
В столбце `telegramFileId` хранится Telegram ID загруженного файла.

#### 3.3. Пример взаимодействия с БД <a name="3.3"></a>
Пример подключения и отправки запроса к БД:
```java
synchronized (mutex) {
	try (var connection = DriverManager.getConnection(dbUrl)) {
		var statement = connection.prepareStatement(getUserStateQuery);
		statement.setLong(1, chatId);
		var result = statement.executeQuery();
		return result.next() ? result.getInt(1) : null;
	} catch (SQLException ex) {
		return null;
	}
}

```
