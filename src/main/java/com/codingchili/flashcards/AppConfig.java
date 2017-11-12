package com.codingchili.flashcards;

import java.util.UUID;

import com.codingchili.core.configuration.Configurable;
import com.codingchili.core.files.Configurations;
import com.codingchili.core.listener.Request;
import com.codingchili.core.protocol.Role;
import com.codingchili.core.security.TokenFactory;
import com.codingchili.core.storage.AsyncStorage;
import com.codingchili.core.storage.IndexedMapPersisted;
import com.codingchili.core.storage.IndexedMapVolatile;

/**
 * Stores flashcard application configuration.
 */
public class AppConfig implements Configurable {
    private static final String APPLICATION_JSON = "application.json";
    private static String secret = getSecretOrDefault();
    private TokenFactory tokenFactory = new TokenFactory(secret.getBytes());
    private String storage = IndexedMapVolatile.class.getName();
    private String database = "data";

    public static String db() {
        return get().database;
    }

    public static TokenFactory tokenFactory() {
        return get().tokenFactory;
    }

    public static Role authorize(Request request) {
        if (get().tokenFactory.verifyToken(request.token())) {
            return Role.USER;
        } else {
            return Role.PUBLIC;
        }
    }

    @SuppressWarnings("unchecked")
    public static Class<? extends AsyncStorage> storage() {
        try {
            return (Class<? extends AsyncStorage>) Class.forName(get().getStorage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static AppConfig get() {
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
