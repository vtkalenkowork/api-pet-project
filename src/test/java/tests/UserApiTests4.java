package tests;

import client.UserClient;
import data.UserRequest;
import data.UserResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserApiTests4 {
    //Removing new UserClient() from all tests and adding @BeforeEach
    private UserClient userClient;

    @BeforeEach
    public void setup() {
        userClient = new UserClient();
    }

    @Test
    public void shouldCreateUser() {
        UserRequest user = new UserRequest("Slava", "slava@test.com");

        Response response = userClient.createUser(user);

        assertEquals(201, response.statusCode());

        UserResponse createdUser = response.body().as(UserResponse.class);

        assertNotNull(createdUser.getId());
        assertEquals(user.getName(), createdUser.getName());
        assertEquals(user.getEmail(), createdUser.getEmail());
    }

    @Test
    public void shouldCreateAndCheckUser() {
        UserRequest user = new UserRequest("Slava", "slava@test.com");

        Long userId = userClient.createUser(user).jsonPath().getLong("id");

        Response createdUserResponse = userClient.getUser(userId);

        assertEquals(200, createdUserResponse.statusCode());

        UserResponse createdUser = createdUserResponse.body().as(UserResponse.class);

        assertEquals(userId, createdUser.getId());
        assertEquals(user.getName(), createdUser.getName());
        assertEquals(user.getEmail(), createdUser.getEmail());
    }

    @Test
    public void shouldCreateAndUpdateUser() {
        UserRequest user = new UserRequest("Slava", "slava@test.com");
        Response postResponse = userClient.createUser(user);

        assertEquals(201, postResponse.statusCode());

        Long userId = postResponse.jsonPath().getLong("id");

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
    public void shouldCreateAndDeleteUser() {
        UserRequest user = new UserRequest("Slava", "slava@test.com");
        Response postResponse = userClient.createUser(user);

        assertEquals(201, postResponse.statusCode());

        Long userId = postResponse.jsonPath().getLong("id");

        Response deleteResponse = userClient.deleteUser(userId);

        assertEquals(204, deleteResponse.statusCode());

        Response getResponse = userClient.getUser(userId);

        assertEquals(404, getResponse.statusCode());
    }
}
