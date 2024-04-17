import network.Client;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import src.main.java.Request;
import src.main.java.Response;

import java.math.BigDecimal;

class AttackTest {

    private static final Client client = Client.getInstance();

    @Test
    void login() {
        Request request = new Request();
        request.setPath("car/list");

        Response response = client.execute(request, 61091);

        boolean success = (Boolean) response.getBody().get("success");
        String message = (String) response.getBody().get("message");

        Assertions.assertFalse(success);
        Assertions.assertEquals(message, "Bloqueado pela politica de CORS.");
    }
}