package com.codingchili.flashcards.model;

import com.codingchili.core.context.TimerSource;
import com.codingchili.core.logging.Logger;
import com.codingchili.core.storage.AsyncStorage;
import com.codingchili.core.storage.EntryWatcher;
import com.codingchili.core.storage.exception.NothingToRemoveException;
import com.codingchili.core.storage.exception.ValueAlreadyPresentException;
import io.vertx.core.Future;

import java.time.Instant;
import java.util.Collection;

/**
 * Database to keep track of voting events.
 */
public class VoterEventDB implements VoterEventStore {
    private static final Integer EVENT_TTL_SECONDS = 15;
    private static final Integer POLL_RATE_MS = 8000;
    private Logger logger;
    private AsyncStorage<VoterEvent> voters;

    VoterEventDB(AsyncStorage<VoterEvent> voters) {
        this.voters = voters;
        this.logger = voters.context().logger(getClass());

        new EntryWatcher<>(voters, () -> voters.query(VoterEvent.CREATED)
                .between(0L, getLastValidSecond())
                .setName("removeOldVoteEvents"), TimerSource.of(POLL_RATE_MS)).start(this::remove);
    }

    private Long getLastValidSecond() {
        return Instant.now().getEpochSecond() - EVENT_TTL_SECONDS;
    }

    private void remove(Collection<VoterEvent> events) {
        events.forEach(event -> {
            voters.remove(event.getId(), Future.<Void>future().setHandler(done -> {
                if (done.failed() && !(done.cause() instanceof NothingToRemoveException)) {
                    logger.onError(done.cause());
                }
            }));
        });
    }

    @Override
    public Future<Void> addIfAllowedElseFail(VoterEvent event) {
        Future<Void> future = Future.future();
        voters.putIfAbsent(event, done -> {
            if (done.failed()) {
                if (done.cause() instanceof ValueAlreadyPresentException) {
                    future.fail("Sorry, you have already voted recently.");
                } else {
                    future.fail(done.cause());
                }
            } else {
                future.complete();
            }
        });
        return future;
    }
}