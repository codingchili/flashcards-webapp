package com.codingchili.flashcards.model;

import com.codingchili.core.context.CoreContext;
import com.codingchili.core.storage.StorageLoader;
import io.vertx.core.Future;

public interface VoterEventStore {
    /**
     * Creates a new storage for voter events.
     *
     * @param core context to use.
     * @return callback.
     */
    static Future<VoterEventDB> create(CoreContext core) {
        Future<VoterEventDB> future = Future.future();
        new StorageLoader<VoterEvent>().memIndex(core)
                .withValue(VoterEvent.class)
                .build(done -> {
                    if (done.succeeded()) {
                        future.complete(new VoterEventDB(done.result()));
                    } else {
                        future.fail(done.cause());
                    }
                });
        return future;
    }

    /**
     * Fails if an entry with the voter user and category id exists
     * within the last configured time.
     *
     * @param event the event to add if allowed.
     * @return callback.
     */
    Future<Void> addIfAllowedElseFail(VoterEvent event);
}
