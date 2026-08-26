package tests.user;

import client.UserClient;
import data.UserRequest;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserContractTests {
    private UserClient userClient;
    private List<Long> userIds;

    @BeforeEach
    public void setup() {
        userClient = new UserClient();
        userIds = new ArrayList<>();
    }

    @Test
    public void shouldReturnUsersMatchingSchema() {
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
        response
                .then()
                .body(matchesJsonSchemaInClasspath("schemas/users-response-schema.json"));
    }

    @Test
    public void shouldReturnErrorMatchingSchema() {
        UserRequest userRequest = new UserRequest(null, "test@test.com");
        Response response = userClient.createUser(userRequest);
        assertEquals(400, response.statusCode());
        response
                .then()
                .body(matchesJsonSchemaInClasspath("schemas/error-response-schema.json"));
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
