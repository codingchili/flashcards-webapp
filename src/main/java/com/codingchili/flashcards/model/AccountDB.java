package com.codingchili.flashcards.model;

import com.codingchili.core.context.CoreContext;
import com.codingchili.core.security.HashHelper;
import com.codingchili.core.security.Token;
import com.codingchili.core.security.TokenFactory;
import com.codingchili.core.storage.AsyncStorage;
import com.codingchili.core.storage.StorageLoader;
import com.codingchili.flashcards.AppConfig;
import io.vertx.core.Future;

import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

import static com.codingchili.core.configuration.CoreStrings.ID_USERNAME;

/**
 * Stores accounts in an AsyncStorage.
 */
public class AccountDB implements AsyncAccountStore {
    private TokenFactory factory = AppConfig.tokenFactory();
    private AsyncStorage<FlashAccount> accounts;
    private HashHelper hasher;

    public AccountDB(CoreContext core) {
        this.hasher = new HashHelper(core);
        new StorageLoader<FlashAccount>(core)
                .withPlugin(AppConfig.storage())
                .withDB(AppConfig.db(), "accounts")
                .withValue(FlashAccount.class)
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
    public Future<Token> register(FlashAccount account) {
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

    private void sendToken(Future<Token> future, FlashAccount account) {
        Token token = new Token();
        token.setDomain(account.getUsername());
        token.setProperties(account.getProperties());
        token.addProperty("issued", Instant.now().getEpochSecond());
        factory.sign(token);
        future.complete(token);
    }

    @Override
    public Future<Collection<FlashAccount>> search(String username) {
        Future<Collection<FlashAccount>> future = Future.future();
        accounts.query(ID_USERNAME).startsWith(username)
                .pageSize(16)
                .orderBy(ID_USERNAME)
                .execute(done -> {
                    if (done.succeeded()) {
                        future.complete(done.result().stream()
                                .peek(account -> {
                                    account.setInbox(null);
                                    account.setPassword(null);
                                })
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

    @Override
    public Future<Void> message(String receiver, AccountMessage message) {
        Future<Void> future = Future.future();
        accounts.get(receiver, get -> {
            if (get.succeeded()) {
                FlashAccount account = get.result();
                account.addMessage(message);
                accounts.update(account, future);
            } else {
                future.fail(get.cause());
            }
        });
        return future;
    }

    @Override
    public Future<Collection<AccountMessage>> inbox(String username) {
        Future<Collection<AccountMessage>> future = Future.future();
        accounts.get(username, done -> {
            if (done.succeeded()) {
                future.complete(done.result().getInbox());
            } else {
                future.fail(done.cause());
            }
        });
        return future;
    }

    @Override
    public Future<Collection<AccountMessage>> read(String username, String messageId) {
        Future<Collection<AccountMessage>> future = Future.future();
        accounts.get(username, get -> {
            if (get.succeeded()) {
                FlashAccount account = get.result();
                account.readMessage(messageId);
                accounts.update(account, updated -> {
                    if (updated.succeeded()) {
                        future.complete(account.getInbox());
                    } else {
                        future.fail(updated.cause());
                    }
                });
            } else {
                future.fail(get.cause());
            }
        });
        return future;
    }
}
