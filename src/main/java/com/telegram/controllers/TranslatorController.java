package com.telegram.controllers;

import com.github.kshashov.telegram.api.TelegramMvcController;
import com.github.kshashov.telegram.api.bind.annotation.BotController;
import com.github.kshashov.telegram.api.bind.annotation.BotPathVariable;
import com.github.kshashov.telegram.api.bind.annotation.request.MessageRequest;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Component
@BotController
public class TranslatorController implements TelegramMvcController {

    @Value("${bot.token}")
    private String botToken;

    @Override
    public String getToken() {
        return botToken;
    }

    List<String> toTwenty;
    List<String> toHundred;
    List<String> toThousand;

    List<String> toTenThousand;

    public TranslatorController() {
        toTwenty = new ArrayList<>();
        toTwenty.add("");
        toTwenty.add("один ");
        toTwenty.add("два ");
        toTwenty.add("три ");
        toTwenty.add("чотири ");
        toTwenty.add("п'ять ");
        toTwenty.add("шiсть ");
        toTwenty.add("сiм ");
        toTwenty.add("вiсiм ");
        toTwenty.add("дев'ять ");
        toTwenty.add("десять ");
        toTwenty.add("одинадцять ");
        toTwenty.add("дванадцять ");
        toTwenty.add("тринадцять ");
        toTwenty.add("чотирнадцять ");
        toTwenty.add("п'ятнадцять ");
        toTwenty.add("шiстнадцять ");
        toTwenty.add("сiмнадцять ");
        toTwenty.add("вiсiмнадцять ");
        toTwenty.add("дев'ятнадцять ");
        toTwenty.add("двадцять ");

        toHundred = new ArrayList<>();
        toHundred.add("");
        toHundred.add("десять ");
        toHundred.add("двадцять ");
        toHundred.add("тридцять ");
        toHundred.add("сорок ");
        toHundred.add("п'ятдесят ");
        toHundred.add("шiстдесят ");
        toHundred.add("сiмдесят ");
        toHundred.add("вiсiмдесят ");
        toHundred.add("дев'яносто ");

        toThousand = new ArrayList<>();
        toThousand.add("");
        toThousand.add("сто ");
        toThousand.add("двiстi ");
        toThousand.add("триста ");
        toThousand.add("чотириста ");
        toThousand.add("п'ятсот ");
        toThousand.add("шiстсот ");
        toThousand.add("сiмсот ");
        toThousand.add("вiсiмсот ");
        toThousand.add("дев'ятсот ");

        toTenThousand = new ArrayList<>();
        toTenThousand.add("");
        toTenThousand.add("одна ");
        toTenThousand.add("двi ");

    }

    @MessageRequest("{value:[\\S]+}")
    public BaseRequest translate(@BotPathVariable("value") String value, Chat chat) {
        Optional<Double> parsedValue = Optional.of(Double.valueOf(value));
        String answer = extracted(parsedValue);
        char c[] = answer.toCharArray();
        c[0] = Character.toUpperCase(c[0]);
        answer = new String(c);
        return new SendMessage(chat.id(), answer);

    }

    private String extracted(Optional<Double> parsedValue) {
        StringBuilder text = new StringBuilder();

        double digit = parsedValue.get();
        int temp;

        if (digit != 0 && digit <= 1000000) {
            if (digit >= 100000) {
                temp = (int) digit / 1000;
                getTextToThousand(text, temp);
                addedWord(text, temp);
                getTextToThousand(text, (int) (digit - temp * 1000));

            } else if (digit < 100000 && digit >= 1000) {
                getTextToHundred(text, (int) digit);
                addedWord(text, (int) digit / 1000);
                getTextToThousand(text, (int) (digit - ((int) digit / 1000) * 1000));
            } else {
                getTextToThousand(text, (int) digit);
            }
        } else {
            text.append("Ми так не домовлялись. Рахуємо до мільйона.");
        }
        System.out.println(text);
        return text.toString();
    }


    private void getTextToThousand(StringBuilder text, int temp) {
        if (temp >= 100) {
            text.append(toThousand.get(temp / 100));
            temp -= (temp / 100) * 100;
        }
        getTextToHundred(text, temp);
    }

    private static void addedWord(StringBuilder text, int temp) {
        if (temp % 10 == 1) {
            text.append("тисяча ");
        } else if (temp % 10 >= 2 && temp % 10 <= 4 ) {
            text.append("тисячi ");
        } else if (temp % 10 >= 5 || temp >= 10 || temp == 0)
            text.append("тисяч ");
    }

    private void getTextToHundred(StringBuilder text, int digit) {
        int temp = digit;
        if (digit > 1000) {
            temp = digit / 1000;
        }

        if (digit == 1000) {
            text.append("одна ");
            return;
        }
        if (digit == 2000) {
            text.append("дві ");
            return;
        }

        if (temp >= 21 || temp==1 || temp==2) {
            text.append(toHundred.get((temp / 10) % 10));
            if (!text.isEmpty() && (temp % 10 == 1 || temp % 10 == 2)) {
                text.append(toTenThousand.get(temp % 10));
                return;
            }
            text.append(toTwenty.get(temp % 10));
        } else {
            text.append(toTwenty.get(temp));
        }
    }
}
