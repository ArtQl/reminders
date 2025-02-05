package ru.artq.reminders.api.telegram;

public class MessagesTelegram {
    public final static String START_MESSAGE = """
            🤖🚀 Привет! 🤖🚀
            \nЯ чат-бот который напомнит тебе все то, что ты забыл!!!
            \nИспользуй следующие команды:
            \n/registration - регистрация
            \n/login - войти.
            """;

    public final static String LOGIN_MESSAGE = """
            🤖🚀 Ты успешно вошел в системе! 🤖🚀
            \nИспользуй следующие команды:
            \n/find - найти напоминание.
            \n/new - создать напоминание.
            \n/update - обновить напоминание.
            \n/delete - удалить напоминание.
            """;
}
