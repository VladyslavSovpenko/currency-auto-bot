package com.telegram.controllers;

import com.github.kshashov.telegram.api.TelegramMvcController;
import com.github.kshashov.telegram.api.bind.annotation.BotController;
import com.github.kshashov.telegram.api.bind.annotation.BotPathVariable;
import com.github.kshashov.telegram.api.bind.annotation.request.CallbackQueryRequest;
import com.github.kshashov.telegram.api.bind.annotation.request.MessageRequest;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.EditMessageReplyMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.telegram.bankApi.BankEnum;
import com.telegram.bankApi.CurrencyEnum;
import com.telegram.facade.CashApiRequests;
import com.telegram.facade.CurrencyRate;
import com.telegram.userProfiles.Profiles;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static java.lang.Double.parseDouble;

@Component
@BotController
public class ConverterController implements TelegramMvcController {
    private final Profiles profiles;

    public ConverterController() {
        super();
        profiles = Profiles.getInstance();
    }

    @Value("${bot.token}")
    private String botToken;

    @Override
    public String getToken() {
        return botToken;
    }

    @CallbackQueryRequest("Converter")
    public BaseRequest converter(Chat chat) {
        InlineKeyboardMarkup inlineKeyboardMarkup = getInlineKeyboardMarkup(chat);
        return new SendMessage(chat.id(), "Enter what you convert").replyMarkup(inlineKeyboardMarkup);
    }

    @NotNull
    private InlineKeyboardMarkup getInlineKeyboardMarkup(Chat chat) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        CurrencyEnum originalCurrency = profiles.getProfileSettings(chat.id().toString()).getOriginalCurrency();
        CurrencyEnum targetCurrency = profiles.getProfileSettings(chat.id().toString()).getTargetCurrency();

        for (CurrencyEnum currencyEnum : CurrencyEnum.values()) {
            if (currencyEnum == CurrencyEnum.RUB || currencyEnum == CurrencyEnum.EURUSD) {
            } else {
                inlineKeyboardMarkup.addRow(
                        new InlineKeyboardButton(getCurrencyButton(originalCurrency, currencyEnum))
                                .callbackData("Converter " + "original " + currencyEnum),
                        new InlineKeyboardButton(getCurrencyButton(targetCurrency, currencyEnum))
                                .callbackData("Converter " + "target " + currencyEnum));
            }
        }

        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Назад").callbackData("/start"));
        return inlineKeyboardMarkup;
    }

    @MessageRequest("{value:[\\S]+}")
    public BaseRequest convertValue(@BotPathVariable("value") String value, Chat chat) {
        Optional<Double> parseDouble = Optional.of(parseDouble(value));
        CurrencyEnum originalCurrency = profiles.getProfileSettings(chat.id().toString()).getOriginalCurrency();
        CurrencyEnum targetCurrency = profiles.getProfileSettings(chat.id().toString()).getTargetCurrency();
        double ratio = getConverse(originalCurrency, targetCurrency, chat.id().toString());

        return new SendMessage(chat.id(), String.format("%4.2f %s is %4.2f %s", parseDouble.get(), originalCurrency, (parseDouble.get() * ratio), targetCurrency));
    }

    private double getConverse(CurrencyEnum from, CurrencyEnum to, String chatUserID) {
        CurrencyRate bankResponse = CashApiRequests.getInstance().getBankResponse(BankEnum.MONOBANK);
        double ratio = 0.0;
        switch (from) {
            case UAH: {
                switch (to) {
                    case USD:
                        ratio = 1 / bankResponse.getRate(CurrencyEnum.USD).getRateSale();
                        break;
                    case EUR:
                        ratio = 1 / bankResponse.getRate(CurrencyEnum.EUR).getRateSale();
                        break;
                    case UAH:
                        ratio = 1;
                        break;
                }
                break;
            }
            case USD: {
                switch (to) {
                    case UAH:
                        ratio = bankResponse.getRate(CurrencyEnum.USD).getRatePurchase();
                        break;
                    case EUR:
                        ratio = 1 / bankResponse.getRate(CurrencyEnum.EURUSD).getRateSale();
                        break;
                    case USD:
                        ratio = 1;
                        break;
                }
                break;
            }
            case EUR: {
                switch (to) {
                    case UAH:
                        ratio = bankResponse.getRate(CurrencyEnum.EUR).getRatePurchase();
                        break;
                    case USD:
                        ratio = bankResponse.getRate(CurrencyEnum.EURUSD).getRateSale();
                        break;
                    case EUR:
                        ratio = 1;
                        break;
                }
                break;
            }
        }
        return ratio;
    }

    @CallbackQueryRequest("Converter {operator:[\\S]+} {currency:[\\S]+}")
    public BaseRequest concreteBankChoose(@BotPathVariable("operator") String operator, @BotPathVariable("currency") String currency, Chat chat, CallbackQuery callbackQuery) {
        if (operator.equals("original")) {
            CurrencyEnum newCurrency = CurrencyEnum.valueOf(currency);
            profiles.getProfileSettings(chat.id().toString()).setOriginalCurrency(newCurrency);
        } else if (operator.equals("target")) {
            CurrencyEnum newCurrency = CurrencyEnum.valueOf(currency);
            profiles.getProfileSettings(chat.id().toString()).setTargetCurrency(newCurrency);
        }
        return new EditMessageReplyMarkup(chat.id(), callbackQuery.message().messageId()).replyMarkup(getInlineKeyboardMarkup(chat));
    }

    private String getCurrencyButton(CurrencyEnum saved, CurrencyEnum current) {
        return saved == current ? current + " ✅" : current.name();
    }
}
