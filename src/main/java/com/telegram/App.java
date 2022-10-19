package com.telegram;

import com.github.kshashov.telegram.api.MessageType;
import com.github.kshashov.telegram.api.TelegramMvcController;
import com.github.kshashov.telegram.api.bind.annotation.BotController;
import com.github.kshashov.telegram.api.bind.annotation.BotRequest;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.telegram.facade.CashApiRequests;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@BotController
@SpringBootApplication
public class App implements TelegramMvcController {
    @Value("${bot.token}")
    private String botToken;

    @BotRequest(value = "/hello", type = {MessageType.CALLBACK_QUERY, MessageType.MESSAGE})
    public BaseRequest hello(User user, Chat chat) {
        return new SendMessage(chat.id(), "Hello, " + user.firstName() + "!");
    }

    @BotRequest(value = "/start", type = {MessageType.CALLBACK_QUERY, MessageType.MESSAGE})
    public BaseRequest start(User user, Chat chat) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Получить информацию").callbackData("Get"))
                .addRow(new InlineKeyboardButton("Конвертер валют").callbackData("Converter"))
                .addRow(new InlineKeyboardButton("Транспорт").callbackData("Vehicle"))
                .addRow(new InlineKeyboardButton("Настройки").callbackData("Settings"));

        return new SendMessage(chat.id(), "Добро пожаловать. Этот бот поможет отслеживать актуальные курсы валют," +
                " электронных валют, производить конвертацию и позволяет следить за своим авто.")
                .replyMarkup(inlineKeyboardMarkup);
    }


    public static void main(String[] args) {
        CashApiRequests cashApiRequests = CashApiRequests.getInstance();
        cashApiRequests.cashing();
        SpringApplication.run(App.class);
    }

    @Override
    public String getToken() {
        return botToken;
    }
}
