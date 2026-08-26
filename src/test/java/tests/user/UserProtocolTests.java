package tests.user;

import client.UserClient;
import config.Config;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.notNullValue;

public class UserProtocolTests {
    private Long userId;

    @Test
    public void shouldGetUsersWithJsonAcceptHeader() {
        given(Config.getUserRequestSpec())
                .accept("application/json")
                .when()
                .get("/users")
                .then()
                .statusCode(200)
                .header("Content-Type", containsString("application/json"));
    }

    @Test
    public void shouldCreateUserWithJsonContentType() {
        userId = given(Config.getUserRequestSpec())
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
        given(Config.getUserRequestSpec())
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
