package com.telegram.controllers;

import com.github.kshashov.telegram.api.TelegramMvcController;
import com.github.kshashov.telegram.api.bind.annotation.BotController;
import com.github.kshashov.telegram.api.bind.annotation.BotPathVariable;
import com.github.kshashov.telegram.api.bind.annotation.request.CallbackQueryRequest;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.EditMessageReplyMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.telegram.userProfiles.Profiles;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@BotController
public class NotificationController implements TelegramMvcController {

    @Value("${bot.token}")
    private String botToken;

    private final Profiles profiles;

    public NotificationController() {
        profiles = Profiles.getInstance();
    }

    @Override
    public String getToken() {
        return botToken;
    }

    @CallbackQueryRequest("Time_of_notification")
    public BaseRequest settings(Chat chat) {
        InlineKeyboardMarkup inlineKeyboardMarkup = getInlineKeyboardMarkup(chat);
        return new SendMessage(chat.id(), "Enter your notification time").replyMarkup(inlineKeyboardMarkup);
    }

    @NotNull
    private InlineKeyboardMarkup getInlineKeyboardMarkup(Chat chat) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        int startHour = 9;
        for (int i = 0; i < 3; i++) {

            inlineKeyboardMarkup.addRow(new InlineKeyboardButton(getNotifName(startHour, chat)).callbackData("Notif " + startHour++),
                    new InlineKeyboardButton(getNotifName(startHour, chat)).callbackData("Notif " + startHour++),
                    new InlineKeyboardButton(getNotifName(startHour, chat)).callbackData("Notif " + startHour++));
        }
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(getNotifName(startHour, chat)).callbackData("Notif " + startHour));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Back to settings").callbackData("Settings"));
        return inlineKeyboardMarkup;
    }

    private String getNotifName(int hour, Chat chat) {
        int definedHour = profiles.getProfileSettings(chat.id().toString()).getHourNotification();
        return definedHour == hour ? "✅ " + hour + ":00" : hour + ":00";
    }

    @CallbackQueryRequest("Notif {time:[\\S]+}")
    public BaseRequest concreteBankChoose(@BotPathVariable("time") String time, Chat chat, CallbackQuery callbackQuery) {

        profiles.getProfileSettings(chat.id().toString()).setHourNotification(Integer.parseInt(time));

        System.out.println("Notif " + time);
        return new EditMessageReplyMarkup(chat.id(), callbackQuery.message().messageId()).replyMarkup(getInlineKeyboardMarkup(chat));
    }
}
