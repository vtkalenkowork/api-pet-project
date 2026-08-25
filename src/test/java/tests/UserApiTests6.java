package tests;

import base.BaseTest;
import client.UserClient;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class UserApiTests6 extends BaseTest {
    private UserClient userClient;
    private Long userId;

    @Test
    public void shouldGetUsersWithJsonAcceptHeader() {
        given(requestSpec)
                .accept("application/json")
                .when()
                .get("/users")
                .then()
                .statusCode(200)
                .header("Content-Type", containsString("application/json"));
    }

    @Test
    public void shouldCreateUserWithJsonContentType() {
        userId = given(requestSpec)
                .contentType("application/json")
                .body("""
                        {
                        "name" : "Slava",
                        "email" : "slava@test.com"
                        }
                        """)
                .when()
                .post("/users")
                .then()
                .statusCode(201)
                .header("Content-Type", containsString("application/json"))
                .body("id", notNullValue())
                .extract()
                .response()
                .jsonPath()
                .getLong("id");
    }

    @Test
    public void shouldReturnErrorWithUnsupportedContentType() {
        given(requestSpec)
                .contentType("text/plain")
                .body("""
                        {
                        "name" : "Slava",
                        "email" : "slava@test.com"
                        }
                        """)
                .when()
                .post("/users")
                .then()
                .statusCode(415)
                .header("Content-Type", containsString("application/json"));
    }

    @AfterEach
    public void tearDown() {
        if (userId != null) {
            new UserClient().deleteUser(userId);
        }
    }
}
