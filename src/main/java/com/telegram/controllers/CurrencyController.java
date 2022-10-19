package com.telegram.controllers;

import com.github.kshashov.telegram.api.TelegramMvcController;
import com.github.kshashov.telegram.api.bind.annotation.BotController;
import com.github.kshashov.telegram.api.bind.annotation.request.CallbackQueryRequest;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.telegram.facade.CashApiRequests;
import com.telegram.userProfiles.Profiles;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@BotController
public class CurrencyController implements TelegramMvcController {
    @Value("${bot.token}")
    private String botToken;

    @Override
    public String getToken() {
        return botToken;
    }

    @CallbackQueryRequest("Get")
    public BaseRequest settings(Chat chat) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup()
                .addRow(new InlineKeyboardButton("Назад").callbackData("/start"));
        return new SendMessage(chat.id(), CashApiRequests
                .getNotificationForUser(Profiles.getInstance().getProfileSettings(chat.id().toString()))).replyMarkup(inlineKeyboardMarkup);
    }
}
