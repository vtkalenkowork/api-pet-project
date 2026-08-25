package tests;

import base.BaseTest;
import client.UserClient;
import data.UserRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class UserApiTests7 extends BaseTest {

    UserClient userClient;
    List<Long> userIds;

    @BeforeEach
    public void setUp() {
        userClient = new UserClient();
        userIds = new ArrayList<>();
    }

    //Testing query params
    @Test
    public void shouldFilterUsersByName() {
        given(requestSpec)
                .queryParam("name", "Slava")
                .when()
                .get("/users")
                .then()
                .statusCode(200)
                .body("", not(empty()))
                .body("name", everyItem(equalTo("Slava")));
    }

    @Test
    public void shouldCreatedAndFilterUsersByName() {
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

        given(requestSpec)
                .queryParam("name", "Slava1")
                .when()
                .get("/users")
                .then()
                .statusCode(200)
                .body("", hasSize(2))
                .body("name", everyItem(equalTo("Slava1")));
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
