import com.codingchili.core.context.SystemContext;
import com.codingchili.core.storage.AsyncStorage;
import com.codingchili.core.storage.IndexedMapPersisted;
import com.codingchili.core.storage.Storable;
import com.codingchili.core.storage.StorageLoader;
import com.codingchili.flashcards.model.FlashCategory;
import com.googlecode.cqengine.ConcurrentIndexedCollection;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.persistence.disk.DiskPersistence;
import com.googlecode.cqengine.persistence.onheap.OnHeapPersistence;
import com.googlecode.cqengine.query.QueryFactory;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collections;

/**
 * Tests for the category handler.
 */
@RunWith(VertxUnitRunner.class)
public class CategoryHandlerTest {

    @Test
    public void testCreate() {
        SystemContext context = new SystemContext();
        new StorageLoader<FlashCategory>(context)
                .withPlugin(IndexedMapPersisted.class.getName())
                .withDB("fifty", "9")
                .withClass(FlashCategory.class)
                .build(storage -> {
                    AsyncStorage<FlashCategory> db = storage.result();

                    FlashCategory cat = new FlashCategory();
                    cat.setName("x");
                    cat.setOwner("z");
                    FlashCategory cat2 = new FlashCategory();
                    cat2.setName("x");
                    cat2.setOwner("z");
                    cat2.setShared(true);

                    System.out.println(cat.hashCode());
                    System.out.println(cat2.hashCode());
                    System.out.println(cat.equals(cat2));
                    System.out.println(cat.id());
                    System.out.println(cat2.id());

                    db.put(cat, done -> {

                        db.put(cat2, done2 -> {
                            db.size(size -> {
                                System.out.println(size.result());
                            });
                        });

                    });
                });
    }

    @Test
    public void testAuthorize() {
        SimpleAttribute<User, String> field =
                QueryFactory.attribute(User.class, String.class, "name", User::getName);
        ConcurrentIndexedCollection<User> db = new ConcurrentIndexedCollection<>(OnHeapPersistence.onPrimaryKey(field));

        User first = new User("user 1");
        User second = new User("user 2");
        db.update(Collections.singleton(first), Collections.singleton(first));
        db.update(Collections.singleton(second), Collections.singleton(second));
        assert db.size() == 1;
    }

    public class User {
        private final String name = "static";
        private final String unique;

        public User(String unique) {
            this.unique = unique;
        }

        public String getName() {
            return name;
        }

        @Override
        public int hashCode() {
            return name.hashCode();
        }

        @Override
        public boolean equals(Object other) {
            return true;
        }
    }

    @Test
    public void testAuthorizeUnauthorized() {

    }

    @Test
    public void testListPublicCategories() {

    }

    @Test
    public void testShareCategory() {

    }

    @Test
    public void testShareCategoryUnauthorized() {

    }

    @Test
    public void testRefreshMemberCategories() {

    }

    @Test
    public void testListAccessible() {

    }

    @Test
    public void testListSize() {

    }
}
