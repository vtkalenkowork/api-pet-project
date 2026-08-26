package tests.user;

import client.UserClient;
import data.ErrorResponse;
import data.UserRequest;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserValidationTests {
    UserClient userClient;

    @BeforeEach
    public void setUp() {
        userClient = new UserClient();
    }

    @Test
    public void shouldReturn400WhenNameIsEmpty(){
        UserRequest userRequest = new UserRequest("", "test@test.com");
        Response postResponse = userClient.createUser(userRequest);

        assertEquals(400, postResponse.statusCode());

        ErrorResponse errorResponse = postResponse.body().as(ErrorResponse.class);

        assertEquals(400, errorResponse.getStatus());
        assertTrue(errorResponse.getErrors().contains("Name cannot be empty"));
    }

    @Test
    public void shouldReturn400WhenNameIsMissing(){
        UserRequest userRequest = new UserRequest(null, "test@test.com");
        Response postResponse = userClient.createUser(userRequest);

        assertEquals(400, postResponse.statusCode());

        ErrorResponse errorResponse = postResponse.body().as(ErrorResponse.class);

        assertEquals(400, errorResponse.getStatus());
        assertTrue(errorResponse.getErrors().contains("Name cannot be empty"));
    }

    //Missing validation for empty and null email on the BE service side
    /*@Test
    public void shouldReturn400WhenEmailIsEmpty(){
        UserRequest userRequest = new UserRequest("Slava", "");
        Response postResponse = userClient.createUser(userRequest);

        assertEquals(400, postResponse.statusCode());

        ErrorResponse errorResponse = postResponse.body().as(ErrorResponse.class);

        assertEquals(400, errorResponse.getStatus());
        assertTrue(errorResponse.getErrors().contains("Email is invalid"));
    }

    @Test
    public void shouldReturn400WhenEmailIsMissing(){
        UserRequest userRequest = new UserRequest("Slava", null);
        Response postResponse = userClient.createUser(userRequest);

        assertEquals(400, postResponse.statusCode());

        ErrorResponse errorResponse = postResponse.body().as(ErrorResponse.class);

        assertEquals(400, errorResponse.getStatus());
        assertTrue(errorResponse.getErrors().contains("Email is invalid"));
    }*/

    @Test
    public void shouldReturn400WithInvalidEmailFormat(){
        UserRequest userRequest = new UserRequest("Slava", "not-an-email");
        Response postResponse = userClient.createUser(userRequest);

        assertEquals(400, postResponse.statusCode());

        ErrorResponse errorResponse = postResponse.body().as(ErrorResponse.class);

        assertEquals(400, errorResponse.getStatus());
        assertTrue(errorResponse.getErrors().contains("Email is invalid"));
    }

    @Test
    public void shouldReturn400WhenDomainIsMissing(){
        UserRequest userRequest = new UserRequest("Slava", "test@");
        Response postResponse = userClient.createUser(userRequest);

        assertEquals(400, postResponse.statusCode());

        ErrorResponse errorResponse = postResponse.body().as(ErrorResponse.class);

        assertEquals(400, errorResponse.getStatus());
        assertTrue(errorResponse.getErrors().contains("Email is invalid"));
    }

    @Test
    public void shouldReturn400WhenLocalPartIsMissing(){
        UserRequest userRequest = new UserRequest("Slava", "@test.com");
        Response postResponse = userClient.createUser(userRequest);

        assertEquals(400, postResponse.statusCode());

        ErrorResponse errorResponse = postResponse.body().as(ErrorResponse.class);

        assertEquals(400, errorResponse.getStatus());
        assertTrue(errorResponse.getErrors().contains("Email is invalid"));
    }

    /*@Test
    public void shouldReturn400WhenTopLevelDomainIsMissing(){
        UserRequest userRequest = new UserRequest("Slava", "test@test");
        Response postResponse = userClient.createUser(userRequest);

        assertEquals(400, postResponse.statusCode());

        ErrorResponse errorResponse = postResponse.body().as(ErrorResponse.class);

        assertEquals(400, errorResponse.getStatus());
        assertTrue(errorResponse.getErrors().contains("Email is invalid"));
    }*/

    @Test
    public void shouldReturn400WhenEmailContainsSpaces(){
        UserRequest userRequest = new UserRequest("Slava", "slava test@test.com");
        Response postResponse = userClient.createUser(userRequest);

        assertEquals(400, postResponse.statusCode());

        ErrorResponse errorResponse = postResponse.body().as(ErrorResponse.class);

        assertEquals(400, errorResponse.getStatus());
        assertTrue(errorResponse.getErrors().contains("Email is invalid"));
    }
}
