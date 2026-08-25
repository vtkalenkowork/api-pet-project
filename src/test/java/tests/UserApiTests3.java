package tests;

import client.UserClient;
import data.UserRequest;
import data.UserResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserApiTests3 {
    //Using user client
    @Test
    public void shouldCreateUser(){
        UserRequest user = new UserRequest("Slava", "slava@test.com");

        Response response = new UserClient().createUser(user);

        assertEquals(201, response.statusCode());

        UserResponse createdUser = response.body().as(UserResponse.class);

        assertNotNull(createdUser.getId());
        assertEquals(user.getName(), createdUser.getName());
        assertEquals(user.getEmail(), createdUser.getEmail());
    }

    @Test
    public void shouldCreateAndCheckUser(){
        UserRequest user = new UserRequest("Slava", "slava@test.com");

        Long userId = new UserClient().createUser(user).jsonPath().getLong("id");

        Response createdUserResponse = new UserClient().getUser(userId);

        assertEquals(200, createdUserResponse.statusCode());

        UserResponse createdUser = createdUserResponse.body().as(UserResponse.class);

        assertEquals(userId, createdUser.getId());
        assertEquals(user.getName(), createdUser.getName());
        assertEquals(user.getEmail(), createdUser.getEmail());
    }

    @Test
    public void shouldCreateAndUpdateUser(){
        UserRequest user = new UserRequest("Slava", "slava@test.com");
        Response postResponse = new UserClient().createUser(user);

        assertEquals(201, postResponse.statusCode());

        Long userId = postResponse.jsonPath().getLong("id");

        UserRequest updatedUser = new UserRequest("Viacheslav", "viacheslav@test.com");
        Response putResponse = new UserClient().updateUser(updatedUser, userId);

        assertEquals(200, putResponse.statusCode());

        Response getResponse = new UserClient().getUser(userId);

        assertEquals(200, getResponse.statusCode());

        UserResponse userResponse = getResponse.body().as(UserResponse.class);

        assertEquals(userId, userResponse.getId());
        assertEquals(updatedUser.getName(), userResponse.getName());
        assertEquals(updatedUser.getEmail(), userResponse.getEmail());
    }

    @Test
    public void shouldCreateAndDeleteUser(){
        UserRequest user = new UserRequest("Slava", "slava@test.com");
        Response postResponse = new UserClient().createUser(user);

        assertEquals(201, postResponse.statusCode());

        Long userId = postResponse.jsonPath().getLong("id");

        Response deleteResponse = new UserClient().deleteUser(userId);

        assertEquals(204, deleteResponse.statusCode());

        Response getResponse = new UserClient().getUser(userId);

        assertEquals(404, getResponse.statusCode());
    }
}
