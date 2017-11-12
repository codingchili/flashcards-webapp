package com.codingchili.flashcards.request;

import com.codingchili.core.listener.Request;
import com.codingchili.core.listener.RequestWrapper;
import com.codingchili.core.protocol.Serializer;
import com.codingchili.core.protocol.exception.AuthorizationRequiredException;
import com.codingchili.core.security.Token;

import static com.codingchili.core.configuration.CoreStrings.*;

/**
 * Request helper class for authentication.
 */
public class AccountRequest extends RequestWrapper {
    public static final String ID_RECEIVER = "receiver";
    public static final String ID_TITLE = "title";
    public static final String ID_BODY = "body";
    public static final String ID_MESSAGE = "message";

    public AccountRequest(Request request) {
        super(request);
    }

    public String username() {
        return data().getString(ID_USERNAME);
    }

    public String authenticatedUser() {
        Token token = Serializer.unpack(data().getJsonObject(ID_TOKEN), Token.class);
        if (token == null) {
            throw new AuthorizationRequiredException();
        }
        return token.getDomain();
    }

    public String password() {
        return data().getString(ID_PASSWORD);
    }

    public String receiver() {
        return data().getString(ID_RECEIVER);
    }

    public String title() {
        return data().getString(ID_TITLE);
    }

    public String body() {
        return data().getString(ID_BODY);
    }

    public String message() {
        return data().getString(ID_MESSAGE);
    }
}
