package com.telegram.notifier;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

public class NotifTimer {
//    Notifier notifier;

//    public NotifTimer(TelegramLongPollingBot bot, Profiles profiles) {
//        this.notifier = new Notifier(bot, profiles);
//    }
//
//    public void startNotifying() {
//        ZonedDateTime startTime = ZonedDateTime.now();
//        startTime = startTime.withHour(startTime.getHour() + 1).withMinute(0).withSecond(0).withNano(0);
//
//        Date startDate = Date.from(startTime.toInstant());
//
//        TimeZone timeZoneUa = TimeZone.getTimeZone("Europe/Kiev");
//        Timer timer = new Timer(true);
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                ZonedDateTime currentTime = ZonedDateTime.now(timeZoneUa.toZoneId());
//                int hour = currentTime.getHour();
//                if (hour >= 9 && hour <= 18) {
//                    notifier.sendNotifications(hour);
//                }
//            }
//        }, startDate, 3600000L);
//    }
}