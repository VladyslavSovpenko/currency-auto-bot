package com.telegram.controllers;

import com.github.kshashov.telegram.api.TelegramMvcController;
import com.github.kshashov.telegram.api.bind.annotation.BotController;
import com.github.kshashov.telegram.api.bind.annotation.BotPathVariable;
import com.github.kshashov.telegram.api.bind.annotation.request.CallbackQueryRequest;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.EditMessageReplyMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.telegram.bankApi.BankEnum;
import com.telegram.bankApi.CurrencyEnum;
import com.telegram.userProfiles.Profiles;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@BotController
public class SettingsController implements TelegramMvcController {

    @Value("${bot.token}")
    private String botToken;

    private final Profiles profiles;

    public SettingsController() {
        profiles = Profiles.getInstance();
    }

    @Override
    public String getToken() {
        return botToken;
    }

    @CallbackQueryRequest("Settings")
    public BaseRequest settings(Chat chat) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup()
                .addRow(new InlineKeyboardButton("Кол-во знаков после запятой").callbackData("Number"))
                .addRow(new InlineKeyboardButton("Банк").callbackData("Bank_enum"))
                .addRow(new InlineKeyboardButton("Валюты").callbackData("currencies"))
                .addRow(new InlineKeyboardButton("Е-Валюты").callbackData("ecurrency"))
                .addRow(new InlineKeyboardButton("Время оповещений").callbackData("Time_of_notification"))
                .addRow(new InlineKeyboardButton("Назад").callbackData("/start"));
        return new SendMessage(chat.id(), "Настройки").replyMarkup(inlineKeyboardMarkup);
    }

    //-------bank part
    @CallbackQueryRequest("Bank_enum")
    public BaseRequest bank(Chat chat) {
        InlineKeyboardMarkup inlineKeyboardMarkup = getBankMenu(chat);
        return new SendMessage(chat.id(), "Перечень доступных банков").replyMarkup(inlineKeyboardMarkup);
    }

    @NotNull
    private InlineKeyboardMarkup getBankMenu(Chat chat) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        Arrays.stream(BankEnum.values()).forEach(bankEnum -> {
            inlineKeyboardMarkup.addRow(new InlineKeyboardButton(getBankEnumButton(bankEnum.name(), String.valueOf(chat.id())))
                    .callbackData("Bank_enum " + bankEnum.getName()));
        });
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Назад").callbackData("Settings"));
        return inlineKeyboardMarkup;
    }

    private BankEnum bankNameConvert(String bankName) {
        for (BankEnum bankEnum : BankEnum.values()) {
            if (bankName.equals(bankEnum.name())) {
                return bankEnum;
            }
        }
        return null;
    }

    @CallbackQueryRequest("Bank_enum {name:[\\S]+}")
    public BaseRequest concreteBankChoose(@BotPathVariable("name") String bankName, Chat chat, CallbackQuery callbackQuery) {
        String id = String.valueOf(chat.id());
        int size = profiles.getProfileSettings(id).getBanks().size();

        String saved = String.valueOf(profiles.getProfileSettings(id).getBanks());
        String[] split = saved.split(",");
        for (int i = 0; i < size; i++) {
            if (split[i].contains(bankName)) {
                if (size != 1) {
                    profiles.getProfileSettings(id).removeBank(bankNameConvert(bankName));
                    break;
                }
            } else {
                profiles.getProfileSettings(id).addBank(bankNameConvert(bankName));
                if (i == size - 1) {
                    break;
                }
            }
        }
        System.out.println("Bank_enum " + bankName);
        return new EditMessageReplyMarkup(chat.id(), callbackQuery.message().messageId()).replyMarkup(getBankMenu(chat));
    }

    private String getBankEnumButton(String current, String chatId) {
        int size = profiles.getProfileSettings(chatId).getBanks().size();
        String result = "";

        for (int i = 0; i < size; i++) {
            String saved = String.valueOf(profiles.getProfileSettings(chatId).getBanks());
            String[] split = saved.split(",");
            if (split[i].contains(current)) {
                result = "✅ " + current;
            } else {
                result = current;
            }
            if (result.contains("✅ ")) {
                return result;
            }
        }
        return result;
    }

    //-------currencies part
    @CallbackQueryRequest("currencies")
    public BaseRequest currencies(Chat chat) {
        InlineKeyboardMarkup inlineKeyboardMarkup = getCurrenciesMenu(chat);
        return new SendMessage(chat.id(), "Перечень доступных валют").replyMarkup(inlineKeyboardMarkup);
    }

    @NotNull
    private InlineKeyboardMarkup getCurrenciesMenu(Chat chat) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        Arrays.stream(CurrencyEnum.values()).forEach(currency -> {
            if (currency.getValue() != "UAH") {
                inlineKeyboardMarkup.addRow(new InlineKeyboardButton(getCurrencyEnumButton(currency.getValue(), String.valueOf(chat.id())))
                        .callbackData("currencies " + currency.getName()));
            }
        });
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Назад").callbackData("Settings"));
        return inlineKeyboardMarkup;
    }

    @CallbackQueryRequest("currencies {name:[\\S]+}")
    public BaseRequest concreteCurrenciesChoose(@BotPathVariable("name") String currName, Chat chat, CallbackQuery callbackQuery) {
        String id = String.valueOf(chat.id());
        int size = profiles.getProfileSettings(id).getCurrencies().size();

        String saved = String.valueOf(profiles.getProfileSettings(id).getCurrencies());
        String[] split = saved.split(",");
        for (int i = 0; i < size; i++) {
            if (split[i].contains(currName)) {
                if (size != 1) {
                    profiles.getProfileSettings(id).removeCurrency(currNameConvert(currName));
                    break;
                }
            } else {
                profiles.getProfileSettings(id).addCurrency(currNameConvert(currName));
                if (i == size - 1) {
                    break;
                }
            }
        }
        System.out.println("currencies " + currName);
        return new EditMessageReplyMarkup(chat.id(), callbackQuery.message().messageId()).replyMarkup(getCurrenciesMenu(chat));
    }

    private CurrencyEnum currNameConvert(String currName) {
        for (CurrencyEnum currEnum : CurrencyEnum.values()) {
            if (currName.equals(currEnum.name())) {
                return currEnum;
            }
        }
        return null;
    }

    private String getCurrencyEnumButton(String current, String chatId) {
        int size = profiles.getProfileSettings(chatId).getCurrencies().size();
        String result = "";

        for (int i = 0; i < size; i++) {
            String saved = String.valueOf(profiles.getProfileSettings(chatId).getCurrencies());
            String[] split = saved.split(",");
            if (current == CurrencyEnum.EURUSD.getValue())
                return "";
            if (split[i].contains(current)) {
                result = "✅ " + current;
            } else {
                result = current;
            }
            if (result.contains("✅ ")) {
                return result;
            }
        }
        return result;
    }
//-------Number part

    @CallbackQueryRequest("Number")
    public BaseRequest coma(Chat chat) {
        InlineKeyboardMarkup inlineKeyboardMarkup = getComaMenu(chat);
        return new SendMessage(chat.id(), "Количество знаков после запятой").replyMarkup(inlineKeyboardMarkup);
    }

    @NotNull
    private InlineKeyboardMarkup getComaMenu(Chat chat) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        for (int i = 2; i < 5; i++) {
            inlineKeyboardMarkup.addRow(new InlineKeyboardButton(getComaButton(i, String.valueOf(chat.id())))
                    .callbackData("Number " + i));
        }
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Назад").callbackData("Settings"));
        return inlineKeyboardMarkup;
    }

    @CallbackQueryRequest("Number {name:[\\d]+}")
    public BaseRequest concreteNumberChoose(@BotPathVariable("name") String currName, Chat chat, CallbackQuery callbackQuery) {
        String id = String.valueOf(chat.id());
        profiles.getProfileSettings(id).setAfterComma(Integer.parseInt(currName));

        System.out.println("Number " + currName);
        return new EditMessageReplyMarkup(chat.id(), callbackQuery.message().messageId()).replyMarkup(getComaMenu(chat));
    }

    private String getComaButton(int current, String chatId) {
        int afterComa = profiles.getProfileSettings(chatId).getAfterComma();
        String result = "";

        if (current == afterComa) {
            result = "✅ " + current;
        } else {
            result = String.valueOf(current);
        }
        if (result.contains("✅ ")) {
            return result;
        }

        return result;
    }
}
