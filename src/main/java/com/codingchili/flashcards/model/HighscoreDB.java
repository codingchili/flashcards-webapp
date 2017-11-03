package com.codingchili.flashcards.model;

import com.codingchili.core.context.CoreContext;
import com.codingchili.core.storage.AsyncStorage;
import com.codingchili.core.storage.StorageLoader;
import com.codingchili.flashcards.AppConfig;
import io.vertx.core.Future;

import java.util.function.BiConsumer;

/**
 * Stores highscores in the database.
 *
 * Highscore entries are grouped into lists:
 *
 * Drawbacks
 * - requires quite a bit more code.
 *
 * Benefits
 * - allows us to more efficiently manage the data
 *  - can remove old entries when new are added without additional queries.
 *  - only one object to lookup in the database.
 */
public class HighscoreDB implements AsyncHighscoreStore {
    private AsyncStorage<HighscoreList> db;

    private HighscoreDB(AsyncStorage<HighscoreList> db) {
        this.db = db;
    }

    public static Future<HighscoreDB> create(CoreContext core) {
        Future<HighscoreDB> future = Future.future();
        new StorageLoader<HighscoreList>(core)
                .withPlugin(AppConfig.storage())
                .withDB(AppConfig.db(), "highscores")
                .withClass(HighscoreList.class).build(done -> {
            if (done.succeeded()) {
                future.complete(new HighscoreDB(done.result()));
            } else {
                future.fail(done.cause());
            }
        });
        return future;
    }

    @Override
    public Future<HighscoreList> add(String cateogory, Highscore highscore) {
        Future<HighscoreList> future = Future.future();

        BiConsumer<HighscoreList, Highscore> save = (list, score) -> {
            list.add(highscore);
            db.put(list, done -> {
                if (done.succeeded()) {
                    future.complete(list);
                } else {
                    future.fail(done.cause());
                }
            });
        };

        db.contains(cateogory, contains -> {
            if (contains.failed()) {
                future.fail(contains.cause());
            } else if (contains.result()) {
                db.get(cateogory, get -> {
                    if (get.succeeded()) {
                        save.accept(get.result(), highscore);
                    } else {
                        future.fail(get.cause());
                    }
                });
            } else {
                save.accept(new HighscoreList().setCategory(cateogory), highscore);
            }
        });

        return future;
    }

    @Override
    public Future<HighscoreList> list(String category) {
        Future<HighscoreList> future = Future.future();
        db.get(category, future);
        return future;
    }
}
