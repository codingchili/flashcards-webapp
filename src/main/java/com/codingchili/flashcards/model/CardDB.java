package com.codingchili.flashcards.model;

import com.codingchili.core.context.CoreContext;
import com.codingchili.core.storage.AsyncStorage;
import com.codingchili.core.storage.StorageLoader;
import com.codingchili.flashcards.AppConfig;
import io.vertx.core.Future;

import java.util.Collection;

import static com.codingchili.flashcards.model.FlashCard.ID_CATEGORY;

/**
 * Manages stored cards.
 */
public class CardDB implements AsyncCardStore {
    private AsyncStorage<FlashCard> cards;

    public CardDB(AsyncStorage<FlashCard> cards) {
        this.cards = cards;
    }

    public CardDB(CoreContext core) {
        new StorageLoader<FlashCard>(core)
                .withPlugin(AppConfig.storage())
                .withClass(FlashCard.class)
                .withDB(AppConfig.db(), "cards")
                .build(done -> cards = done.result());
    }

    @Override
    public Future<Void> add(FlashCard card) {
        Future<Void> future = Future.future();
        cards.put(card, future);
        return future;
    }

    @Override
    public Future<Void> remove(String cardId) {
        Future<Void> future = Future.future();
        cards.remove(cardId, future);
        return future;
    }

    @Override
    public Future<Collection<FlashCard>> get(String category) {
        Future<Collection<FlashCard>> future = Future.future();
        cards.query(ID_CATEGORY)
                .equalTo(category)
                .execute(search -> {
                    if (search.succeeded()) {
                        future.complete(search.result());
                    } else {
                        future.fail(search.cause());
                    }
                });
        return future;
    }

    @Override
    public Future<Integer> size() {
        Future<Integer> future = Future.future();
        cards.size(future);
        return future;
    }
}
