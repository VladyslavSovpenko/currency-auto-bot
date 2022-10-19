package com.telegram.notifier;

public class Notifier {
//
//    private TelegramLongPollingBot bot;
//    private Profiles profiles;
//
//    public Notifier(TelegramLongPollingBot bot, Profiles profiles) {
//        this.bot = bot;
//        this.profiles = profiles;
//    }
//
//    private void sendOneNotification(String chatId, ProfileSettings settings) {
//        SendMessage sendMessage = new SendMessage();
//        sendMessage.setChatId(chatId);
//        String output = CashApiRequests.getNotificationForUser(settings);
//        sendMessage.setText(output);
//        try {
//            bot.execute(sendMessage);
//        }
//        catch (TelegramApiException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void sendNotifications(int hour) {
//        Map<String, ProfileSettings> settings = profiles.getAllProfileSettings();
//        for (String chatId: settings.keySet()) {
//            ProfileSettings value = settings.get(chatId);
//            if (value.getHourNotification() == hour) {
//                sendOneNotification(chatId, value);
//            }
//        }
//    }
}
