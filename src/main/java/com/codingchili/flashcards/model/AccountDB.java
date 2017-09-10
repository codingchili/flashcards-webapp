package com.codingchili.flashcards.model;

import com.codingchili.core.context.CoreContext;
import com.codingchili.core.security.Account;
import com.codingchili.core.security.HashHelper;
import com.codingchili.core.security.Token;
import com.codingchili.core.security.TokenFactory;
import com.codingchili.core.storage.AsyncStorage;
import com.codingchili.core.storage.StorageLoader;
import com.codingchili.flashcards.AppConfig;
import io.vertx.core.Future;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

import static com.codingchili.core.configuration.CoreStrings.ID_USERNAME;

/**
 * Stores accounts in an AsyncStorage.
 */
public class AccountDB implements AsyncAccountStore {
    private TokenFactory factory = AppConfig.factory();
    private AsyncStorage<Account> accounts;
    private HashHelper hasher;

    public AccountDB(CoreContext core) {
        this.hasher = new HashHelper(core);
        new StorageLoader<Account>(core)
                .withPlugin(AppConfig.storage())
                .withDB(AppConfig.db(), "accounts")
                .withClass(Account.class)
                .build(storage -> accounts = storage.result());
    }

    @Override
    public Future<Token> authenticate(String username, String password) {
        Future<Token> future = Future.future();
        accounts.get(username, account -> {
            if (account.succeeded()) {
                hasher.verify(verification -> {
                    if (verification.succeeded()) {
                        sendToken(future, account.result());
                    } else {
                        future.fail(verification.cause());
                    }
                }, account.result().getPassword(), password);
            } else {
                future.fail(account.cause());
            }
        });
        return future;
    }

    @Override
    public Future<Token> register(Account account) {
        Future<Token> future = Future.future();
        hasher.hash(done -> {
            account.setPassword(done.result());
            account.addProperty("roles", Collections.singletonList("user"));
            account.addProperty("created", Instant.now().getEpochSecond());
            accounts.putIfAbsent(account, result -> {
                if (result.succeeded()) {
                    sendToken(future, account);
                } else {
                    future.fail(result.cause());
                }
            });
        }, account.getPassword());
        return future;
    }

    private void sendToken(Future<Token> future, Account account) {
        Token token = new Token();
        token.setDomain(account.getUsername());
        token.setProperties(account.getProperties());
        token.addProperty("issued", Instant.now().getEpochSecond());
        factory.sign(token);
        future.complete(token);
    }

    @Override
    public Future<Collection<Account>> search(String username) {
        Future<Collection<Account>> future = Future.future();
        accounts.query(ID_USERNAME).startsWith(username)
                .pageSize(16)
                .orderBy(ID_USERNAME)
                .execute(done -> {
                    if (done.succeeded()) {
                        future.complete(done.result().stream()
                                .map(account -> account.setPassword(null))
                                .collect(Collectors.toList()));
                    } else {
                        future.fail(done.cause());
                    }
                });
        return future;
    }

    @Override
    public Future<Integer> size() {
        Future<Integer> future = Future.future();
        accounts.size(future);
        return future;
    }
}
