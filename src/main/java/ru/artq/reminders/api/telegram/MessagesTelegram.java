package ru.artq.reminders.api.telegram;

public class MessagesTelegram {
    public static String START_MESSAGE = """
            🤖🚀🦾 Привет! 🤖🚀🦾
            
            Я чат-бот который напомнит тебе все то, что ты забыл!!!
            
            Используй следующие команды:
            
            /login - войти.
            /register - зарегистрироваться.
            /find - найти напоминание.
            /new - создать напоминание.
            /update - обновить напоминание.
            /remove - удалить напоминание.
            """;

    public static String LOGIN_MESSAGE = "Для входа в систему введите /login [email] [password]";
}
