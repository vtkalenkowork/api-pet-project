package tests;

import client.UserClient;
import data.ErrorResponse;
import data.UserRequest;
import data.UserResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserApiTests5 {

    private UserClient userClient;
    //Using global variable userId for all tests and adding AfterEach method for cleanup test data
    private Long userId;

    @BeforeEach
    public void setup(){
        userClient = new UserClient();
    }

    @Test
    public void shouldCreateUser(){
        UserRequest user = new UserRequest("Slava", "slava@test.com");

        Response response = userClient.createUser(user);

        userId = response.jsonPath().getLong("id");

        assertEquals(201, response.statusCode());

        UserResponse createdUser = response.body().as(UserResponse.class);

        assertNotNull(createdUser.getId());
        assertEquals(user.getName(), createdUser.getName());
        assertEquals(user.getEmail(), createdUser.getEmail());
    }

    @Test
    public void shouldCreateAndCheckUser(){
        UserRequest user = new UserRequest("Slava", "slava@test.com");

        userId = userClient.createUser(user).jsonPath().getLong("id");

        Response createdUserResponse = userClient.getUser(userId);

        assertEquals(200, createdUserResponse.statusCode());

        UserResponse createdUser = createdUserResponse.body().as(UserResponse.class);

        assertEquals(userId, createdUser.getId());
        assertEquals(user.getName(), createdUser.getName());
        assertEquals(user.getEmail(), createdUser.getEmail());
    }

    @Test
    public void shouldCreateAndUpdateUser(){
        UserRequest user = new UserRequest("Slava", "slava@test.com");
        Response postResponse = userClient.createUser(user);

        assertEquals(201, postResponse.statusCode());

        userId = postResponse.jsonPath().getLong("id");

        UserRequest updatedUser = new UserRequest("Viacheslav", "viacheslav@test.com");
        Response putResponse = userClient.updateUser(updatedUser, userId);

        assertEquals(200, putResponse.statusCode());

        Response getResponse = userClient.getUser(userId);

        assertEquals(200, getResponse.statusCode());

        UserResponse userResponse = getResponse.body().as(UserResponse.class);

        assertEquals(userId, userResponse.getId());
        assertEquals(updatedUser.getName(), userResponse.getName());
        assertEquals(updatedUser.getEmail(), userResponse.getEmail());
    }

    @Test
    public void shouldCreateAndDeleteUser(){
        UserRequest user = new UserRequest("Slava", "slava@test.com");
        Response postResponse = userClient.createUser(user);

        assertEquals(201, postResponse.statusCode());

        userId = postResponse.jsonPath().getLong("id");

        Response deleteResponse = userClient.deleteUser(userId);

        assertEquals(204, deleteResponse.statusCode());

        Response getResponse = userClient.getUser(userId);

        assertEquals(404, getResponse.statusCode());

        userId = null;
    }

    //Added error response DTO
    @Test
    public void shouldReturnErrorWithEmptyName(){
        UserRequest user = new UserRequest("", "test@test.com");
        Response postResponse = userClient.createUser(user);

        assertEquals(400, postResponse.statusCode());

        ErrorResponse errorResponse = postResponse.body().as(ErrorResponse.class);
        assertEquals(400, errorResponse.getStatus());

        /*assertTrue(postResponse
                .jsonPath()
                .getList("errors", String.class)
                .contains("Name cannot be empty"));*/

        assertTrue(errorResponse.getErrors().contains("Name cannot be empty"));
    }

    @Test
    public void shouldReturnErrorWithoutName(){
        UserRequest user = new UserRequest(null, "test@test.com");
        Response postResponse = userClient.createUser(user);

        assertEquals(400, postResponse.statusCode());
        /*assertTrue(postResponse
                .jsonPath()
                .getList("errors", String.class)
                .contains("Name cannot be empty"));*/

        ErrorResponse errorResponse = postResponse.body().as(ErrorResponse.class);
        assertEquals(400, errorResponse.getStatus());
        assertTrue(errorResponse.getErrors().contains("Name cannot be empty"));
    }

    @AfterEach
    public void tearDown(){
        if (userId != null){
            userClient.deleteUser(userId);
        }
    }
}
