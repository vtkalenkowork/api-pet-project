package tests.auth;

import client.AuthClient;
import data.ErrorResponse;
import data.LoginRequest;
import io.jsonwebtoken.Claims;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.JwtUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LoginTests {
    AuthClient authClient;

    @BeforeEach
    public void setup() {
        authClient = new AuthClient();
    }

    @Test
    public void shouldLoginWithValidCredentials() {
        LoginRequest loginRequest = new LoginRequest("slava", "password");

        Response response = authClient.login(loginRequest);

        String token = response.jsonPath().getString("token");

        assertEquals(200, response.statusCode());
        assertNotNull(token);
    }

    @Test
    public void shouldReturn401WithInvalidCredentials() {
        LoginRequest loginRequest = new LoginRequest("slava", "wrong-password");

        Response response = authClient.login(loginRequest);

        assertEquals(401, response.statusCode());
        //assertNull(response.jsonPath().getString("token")); - will fail because there is no JSON body in response
        //but the next line ensures that we don't have a body at all
        assertTrue(response.getBody().asString().isEmpty());
    }

    @Test
    public void shouldReturn400WhenPasswordIsEmpty() {
        LoginRequest loginRequest = new LoginRequest("slava", "");

        Response response = authClient.login(loginRequest);

        assertEquals(400, response.statusCode());

        ErrorResponse errorResponse = response.body().as(ErrorResponse.class);
        assertEquals(400, errorResponse.getStatus());
        assertEquals("password: must not be blank", errorResponse.getErrors().getFirst());
    }

    @Test
    public void shouldReturn400WhenUsernameIsEmpty() {
        LoginRequest loginRequest = new LoginRequest("", "password");

        Response response = authClient.login(loginRequest);

        assertEquals(400, response.statusCode());

        ErrorResponse errorResponse = response.body().as(ErrorResponse.class);
        assertEquals(400, errorResponse.getStatus());
        assertEquals("username: must not be blank", errorResponse.getErrors().getFirst());
    }

    @Test
    public void shouldReturnValidJwtAfterLogin() {
        LoginRequest loginRequest = new LoginRequest("slava", "password");

        Response response = authClient.login(loginRequest);

        assertEquals(200, response.statusCode());

        String token = response.jsonPath().getString("token");

        Claims claims = JwtUtils.parseToken(token, "my-super-secret-key-that-is-long-enough-for-hs256");

        assertEquals("slava", claims.getSubject());
        assertNotNull(claims.getIssuedAt());
        assertNotNull(claims.getExpiration());
    }
}
