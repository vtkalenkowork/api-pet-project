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

public class UserFilterTests {
    UserClient userClient;
    List<Long> userIds;

    @BeforeEach
    public void setUp() {
        userClient = new UserClient();
        userIds = new ArrayList<>();
    }

    @Test
    public void shouldFilterUsersByName() {
        UserRequest userRequest = new UserRequest("Slava", "slava@test.com");
        Response postResponse = userClient.createUser(userRequest);

        assertEquals(201, postResponse.statusCode());

        Long userId = postResponse.jsonPath().getLong("id");
        userIds.add(userId);

        UserResponse userResponse = postResponse.body().as(UserResponse.class);
        Response getResponse = userClient.getUsersByName(userResponse.getName());

        assertEquals(200, getResponse.statusCode());

        getResponse
                .then()
                .body("", not(empty()))
                .body("name", everyItem(equalTo("Slava")));
    }

    @Test
    public void shouldCreateAndFilterUsersByName() {
        List<UserRequest> users = List.of(
                new UserRequest("Slava1", "slava1@test.com"),
                new UserRequest("Slava1", "slava2@test.com"),
                new UserRequest("John", "john@test.com"),
                new UserRequest("Mike", "mike@test.com")
        );

        for (UserRequest user : users) {
            Long userId = userClient.createUser(user).jsonPath().getLong("id");
            userIds.add(userId);
        }

        Response getResponse = userClient.getUsersByName("Slava1");
        assertEquals(200, getResponse.statusCode());

        getResponse
                .then()
                .body("", hasSize(2))
                .body("name", everyItem(equalTo("Slava1")));
    }

    @Test
    public void shouldReturnEmptyListWhenNoUsersMatchName(){
        List<UserRequest> users = List.of(
                new UserRequest("Slava1", "slava1@test.com"),
                new UserRequest("Slava1", "slava2@test.com"),
                new UserRequest("John", "john@test.com"),
                new UserRequest("Mike", "mike@test.com")
        );

        for (UserRequest user : users) {
            Long userId = userClient.createUser(user).jsonPath().getLong("id");
            userIds.add(userId);
        }

        Response getResponse = userClient.getUsersByName("Max");
        assertEquals(200, getResponse.statusCode());

        getResponse
                .then()
                .body("", is(empty()));
    }

    @AfterEach
    public void tearDown() {
        for (Long userId : userIds) {
            if (userId != null) {
                userClient.deleteUser(userId);
            }
        }
    }
}
