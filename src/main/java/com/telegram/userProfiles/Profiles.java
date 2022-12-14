package com.telegram.userProfiles;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Profiles implements Serializable {

    private static Profiles instance;
    private final ConcurrentMap<String, ProfileSettings> mapProfiles;

    private Profiles() {
        mapProfiles = new ConcurrentHashMap<>();
    }

    /**
     * получает экземпляр Profiles из сериализованного файла или, если файла нет - создает новый
     */
    public static Profiles getInstance() {
        if (instance == null) {
           instance = new Profiles();
            }

        return  instance;
    }

    /**
     * получает ProfileSettings из мапы или, если нет - создает новый, возвращает его и добавляет в мапу
     */
    public ProfileSettings getProfileSettings(String chatId) {
        return Optional
                .ofNullable(mapProfiles.get(chatId))
                .orElseGet(() -> getDefaultProfileSettings(chatId));
    }
    /**
     * получает все ProfileSettings из мапы
     */
    public Map<String, ProfileSettings> getAllProfileSettings() {
        return new HashMap<>(mapProfiles);
    }

    private void updateProfileSettings(String chatId, ProfileSettings profileSettings) {
        mapProfiles.put(chatId, profileSettings);
    }

    private ProfileSettings getDefaultProfileSettings(String chatId) {
        ProfileSettings profileSettings = new ProfileSettings();
        updateProfileSettings(chatId, profileSettings);
        return profileSettings;
    }

    /**
     * сохраняет текущий экземпляр Profiles в сериализованный файл по определенному расписанию (каждые 5 минут)
     */
    public void SchedulerSaveToFile() {
        Timer timer = new Timer(true);
        timer.schedule(new TimerTask() {

            @Override
            public void run() {

            }
        }, 1000L, 5L * 60L * 1000L);

    }

    public void setProfileSettings(String chatId, ProfileSettings profileSettings) {
        updateProfileSettings(chatId, profileSettings);
    }
}