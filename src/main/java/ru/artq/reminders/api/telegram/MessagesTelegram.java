package ru.artq.reminders.api.telegram;

public class MessagesTelegram {
    public final static String START_MESSAGE = """
            🤖🚀🦾 Привет! 🤖🚀🦾
            \nЯ чат-бот который напомнит тебе все то, что ты забыл!!!
            \nИспользуй следующие команды:
            \n/registration - регистрация
            \n/login - войти.
            \n/find - найти напоминание.
            \n/new - создать напоминание.
            \n/update - обновить напоминание.
            \n/remove - удалить напоминание.
            """;

    public final static String NO_LOGIN_MESSAGE = "Ты еще не вошел в системе!\nДля входа в систему используй: /login [email] [password]";

    public final static String LOGIN_MESSAGE = """
            Вы уже успешно вошли в системе!
            \nИспользуй следующие команды:
            \n/find - найти напоминание.
            \n/new - создать напоминание.
            \n/update - обновить напоминание.
            \n/remove - удалить напоминание.
            """;
}
