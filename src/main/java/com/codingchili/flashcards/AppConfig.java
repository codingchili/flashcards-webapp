package com.codingchili.flashcards;

import java.util.UUID;

import com.codingchili.core.configuration.Configurable;
import com.codingchili.core.files.Configurations;
import com.codingchili.core.security.TokenFactory;
import com.codingchili.core.storage.AsyncStorage;
import com.codingchili.core.storage.IndexedMapPersisted;

/**
 * Stores flashcard application configuration.
 */
public class AppConfig implements Configurable {
    private static final String APPLICATION_JSON = "application.json";
    private static String secret = getSecretOrDefault();
    private String storage = IndexedMapPersisted.class.getName();
    private String database = "data";

    public static String db() {
        return settings().database;
    }

    public static TokenFactory tokenFactory() {
        return new TokenFactory(secret.getBytes());
    }

    @SuppressWarnings("unchecked")
    public static Class<? extends AsyncStorage> storage() {
        try {
            return (Class<? extends AsyncStorage>) Class.forName(settings().getStorage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static AppConfig settings() {
        return Configurations.get(APPLICATION_JSON, AppConfig.class);
    }

    @Override
    public String getPath() {
        return APPLICATION_JSON;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        AppConfig.secret = secret;
    }

    public String getStorage() {
        return storage;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    private static String getSecretOrDefault() {
        String secret = System.getProperty("secret");
        if (secret == null) {
            secret = UUID.randomUUID().toString();
        }
        return secret;
    }
}
