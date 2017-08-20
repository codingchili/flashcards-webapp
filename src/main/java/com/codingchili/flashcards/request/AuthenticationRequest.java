package com.codingchili.flashcards.request;

import com.codingchili.core.listener.Request;
import com.codingchili.core.listener.RequestWrapper;

import static com.codingchili.core.configuration.CoreStrings.ID_PASSWORD;
import static com.codingchili.core.configuration.CoreStrings.ID_USERNAME;

/**
 * Request helper class for authentication.
 */
public class AuthenticationRequest extends RequestWrapper {

    public AuthenticationRequest(Request request) {
        super(request);
    }

    public String username() {
        return data().getString(ID_USERNAME);
    }

    public String password() {
        return data().getString(ID_PASSWORD);
    }
}
