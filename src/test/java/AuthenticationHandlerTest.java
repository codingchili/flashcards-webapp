import com.codingchili.core.protocol.ResponseStatus;
import com.codingchili.core.testing.RequestMock;
import com.codingchili.core.testing.ResponseListener;
import com.codingchili.flashcards.handlers.AuthenticationHandler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.codingchili.core.configuration.CoreStrings.ID_PASSWORD;
import static com.codingchili.core.configuration.CoreStrings.ID_USERNAME;

/**
 * Tests for the authentication handler.
 */
@RunWith(VertxUnitRunner.class)
public class AuthenticationHandlerTest {
    private AuthenticationHandler handler = new AuthenticationHandler();

    @Test
    public void authenticate(TestContext test) {
/*        handle("authenticate", (data, status) -> {
            System.out.println(data.encodePrettily());
            test.assertEquals(status, ResponseStatus.ACCEPTED);
        }, new JsonObject()
            .put(ID_USERNAME, "user")
            .put(ID_PASSWORD, "pw"));*/
    }

    @Test
    public void testFailAuthenticatePassword() {

    }

    @Test
    public void testFailAuthenticateMissing() {

    }

    @Test
    public void testRegister() {

    }

    @Test
    public void testFailRegisterExists() {

    }

    @Test
    public void testSearchUsers() {

    }

    @Test
    public void testSize() {

    }

    private void handle(String route, ResponseListener listener, JsonObject data) {
        handler.handle(RequestMock.get(route, listener, data));
    }
}
