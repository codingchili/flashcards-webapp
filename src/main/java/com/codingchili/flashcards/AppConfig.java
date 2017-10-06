package com.codingchili.flashcards;

import com.codingchili.core.configuration.BaseConfigurable;
import com.codingchili.core.files.Configurations;
import com.codingchili.core.security.TokenFactory;
import com.codingchili.core.storage.IndexedMapPersisted;

import java.util.UUID;

/**
 * Stores flashcard application configuration.
 */
public class AppConfig extends BaseConfigurable {
    private static final String APPLICATION_JSON = "application.json";
    private static String secret = getSecretOrDefault();
    private String storage = IndexedMapPersisted.class.getName();
    private String database = "flashcards";

    public static String db() {
        return settings().database;
    }

    public static TokenFactory factory() {
        return new TokenFactory(secret.getBytes());
    }

    public static Class storage() {
        try {
            return Class.forName(settings().getStorage());
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
