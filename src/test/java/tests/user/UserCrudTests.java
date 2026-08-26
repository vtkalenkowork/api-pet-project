package tests.user;

import client.UserClient;
import data.UserRequest;
import data.UserResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserCrudTests {

    private UserClient userClient;
    private List<Long> userIds;

    @BeforeEach
    public void setup() {
        userClient = new UserClient();
        userIds = new ArrayList<>();
    }

    @Test
    public void shouldReturnAllUsers() {
        List<UserRequest> listOfUsers = List.of(
                new UserRequest("Slava", "slava@test.com"),
                new UserRequest("John", "john@test.com"),
                new UserRequest("Mike", "mike@test.com")
        );

        for (UserRequest user : listOfUsers) {
            Long userId = userClient.createUser(user).jsonPath().getLong("id");
            userIds.add(userId);
        }

        Response response = userClient.getAllUsers();

        assertEquals(200, response.statusCode());

        response.then()
                .body("id", everyItem(notNullValue()))
                .body("name", everyItem(notNullValue()))
                .body("email", everyItem(notNullValue()));
    }

    @Test
    public void shouldCreateAndCheckUser(){
        UserRequest userRequest = new UserRequest("Slava", "slava@test.com");
        Response postResponse = userClient.createUser(userRequest);

        assertEquals(201, postResponse.statusCode());

        Long userId = postResponse.jsonPath().getLong("id");
        userIds.add(userId);
        Response getResponse = userClient.getUser(userId);

        assertEquals(200, getResponse.statusCode());

        UserResponse userResponse = getResponse.body().as(UserResponse.class);
        assertEquals(userId, userResponse.getId());
        assertEquals("Slava", userResponse.getName());
        assertEquals("slava@test.com", userResponse.getEmail());
    }

    @Test
    public void shouldCreateAndUpdateUser() {
        UserRequest user = new UserRequest("Slava", "slava@test.com");
        Response postResponse = userClient.createUser(user);

        assertEquals(201, postResponse.statusCode());

        Long userId = postResponse.jsonPath().getLong("id");
        userIds.add(userId);

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
        userIds.add(userId);

        Response deleteResponse = userClient.deleteUser(userId);

        assertEquals(204, deleteResponse.statusCode());

        Response getResponse = userClient.getUser(userId);

        assertEquals(404, getResponse.statusCode());
    }

    @Test
    public void shouldReturn404ForNotExistingUser() {
        Response getResponse = userClient.getUser(999L);

        assertEquals(404, getResponse.statusCode());
    }

    /*@AfterEach
    public void tearDown() {
        for (Long userId : userIds) {
            if (userId != null) {
                userClient.deleteUser(userId);
            }
        }
    }*/
}
