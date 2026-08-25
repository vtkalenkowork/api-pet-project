package tests;

import client.UserClient;
import config.Config;
import data.ErrorResponse;
import data.UserRequest;
import data.UserResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class UserApiTests8 {
    private UserClient userClient;

    @BeforeEach
    public void setup(){
        userClient = new UserClient();
    }

    @Test
    public void shouldCreateUser(){
        UserRequest userRequest = new UserRequest("Slava", "slava@test.com");
        Response postResponse = userClient.createUser(userRequest);

        assertEquals(201, postResponse.statusCode());
        assertEquals("Slava", postResponse.jsonPath().getString("name"));
        assertEquals("slava@test.com", postResponse.jsonPath().getString("email"));
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

    @Test
    public void shouldReturn400WhenTopLevelDomainIsMissing(){
        UserRequest userRequest = new UserRequest("Slava", "test@test");
        Response postResponse = userClient.createUser(userRequest);

        assertEquals(400, postResponse.statusCode());

        ErrorResponse errorResponse = postResponse.body().as(ErrorResponse.class);

        assertEquals(400, errorResponse.getStatus());
        assertTrue(errorResponse.getErrors().contains("Email is invalid"));
    }

    @Test
    public void shouldReturn400WhenEmailContainsSpaces(){
        UserRequest userRequest = new UserRequest("Slava", "slava test@test.com");
        Response postResponse = userClient.createUser(userRequest);

        assertEquals(400, postResponse.statusCode());

        ErrorResponse errorResponse = postResponse.body().as(ErrorResponse.class);

        assertEquals(400, errorResponse.getStatus());
        assertTrue(errorResponse.getErrors().contains("Email is invalid"));
    }

    @Test
    public void shouldCreateAndCheckUser(){
        UserRequest userRequest = new UserRequest("slava", "slava@test.com");
        Response postResponse = userClient.createUser(userRequest);

        assertEquals(201, postResponse.statusCode());

        Long userId = postResponse.jsonPath().getLong("id");

        Response getResponse = userClient.getUser(userId);

        assertEquals(200, getResponse.statusCode());

        UserResponse userResponse = getResponse.body().as(UserResponse.class);

        assertEquals(userId, userResponse.getId());
        assertEquals("slava", userResponse.getName());
        assertEquals("slava@test.com", userResponse.getEmail());
    }

    @Test
    public void shouldReturn404ForNotExistingUser(){
        Response getResponse = userClient.getUser(999L);

        assertEquals(404, getResponse.statusCode());
    }

    @Test
    public void shouldReturnExistingUser(){
        Response getResponse = userClient.getUser(1L);

        assertEquals(200, getResponse.statusCode());

        UserResponse userResponse = getResponse.body().as(UserResponse.class);
        assertNotNull(userResponse.getId());
        assertNotNull(userResponse.getName());
        assertNotNull(userResponse.getEmail());
    }

    @Test
    public void shouldReturnUsersMatchingSchema() {
        given(Config.getUserRequestSpec())
                .when()
                .get("/users")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath(
                        "schemas/users-response-schema.json"
                ));
    }

    @Test
    public void shouldReturnErrorMatchingSchema() {
        UserRequest userRequest = new UserRequest(null, "test@test.com");

        given(Config.getUserRequestSpec())
                .body(userRequest)
                .when()
                .post("/users")
                .then()
                .statusCode(400)
                .body(matchesJsonSchemaInClasspath(
                        "schemas/error-response-schema.json"
                ));
    }
}
