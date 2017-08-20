import com.codingchili.core.context.CoreContext;
import com.codingchili.core.context.SystemContext;
import com.codingchili.flashcards.handlers.CardHandler;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests for the card handler.
 */
@RunWith(VertxUnitRunner.class)
public class CardHandlerTest {
    private CardHandler handler;
    private CoreContext context;

    @Before
    public void setUp() {
        context = new SystemContext();
        handler = new CardHandler();
        handler.init(context);
    }

    @After
    public void tearDown() {
        context.vertx().close();
    }

    @Test
    public void testAddCard() {

    }

    @Test
    public void testRemoveCard() {

    }

    @Test
    public void testRemoveCardUnauthorized() {

    }

    @Test
    public void testGetCardsInCategory() {

    }

    @Test
    public void testGetUnauthorized() {

    }

    @Test
    public void testGetPublicCard() {

    }

    @Test
    public void testSize() {

    }


}
