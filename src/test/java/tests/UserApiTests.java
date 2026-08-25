package tests;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class UserApiTests {
    @Test
    public void shouldReturnAllUsers() {
        given()
                .when()
                .get("http://localhost:8080/users")
                .then()
                .statusCode(200)
                .contentType("application/json")
                .body("", not(empty()))
                .body("[0].id", notNullValue())
                .body("[0].name", not(empty()))
                .body("[0].email", not(empty()));
    }

    @Test
    public void shouldCreateUser() {
        given()
                .contentType("application/json")
                .body("""
                        {
                        "name" : "Slava",
                        "email" : "slava@test.com"
                        }
                        """)
                .when()
                .post("http://localhost:8080/users")
                .then()
                .statusCode(201)
                .contentType("application/json")
                .body("id", notNullValue())
                .body("name", equalTo("Slava"))
                .body("email", equalTo("slava@test.com"));
    }

    @Test
    public void shouldCreateUserAndCheckItExists() {
        Number userId = given()
                .contentType("application/json")
                .body("""
                        {
                        "name" : "Slava",
                        "email" : "slava@test.com"
                        }
                        """)
                .when()
                .post("http://localhost:8080/users")
                .then()
                .statusCode(201)
                .contentType("application/json")
                .body("id", notNullValue())
                .body("name", equalTo("Slava"))
                .body("email", equalTo("slava@test.com"))
                .extract()
                .path("id");

        given()
                .when()
                .get("http://localhost:8080/users/" + userId)
                .then()
                .statusCode(200)
                .body("id", equalTo(userId))
                .body("name", equalTo("Slava"))
                .body("email", equalTo("slava@test.com"));
    }

    @Test
    public void shouldCheckFullUserFlow() {
        Number userId = given()
                .contentType("application/json")
                .body("""
                        {
                        "name" : "Slava",
                        "email" : "slava@test.com"
                        }
                        """)
                .when()
                .post("http://localhost:8080/users")
                .then()
                .statusCode(201)
                .contentType("application/json")
                .body("id", notNullValue())
                .body("name", equalTo("Slava"))
                .body("email", equalTo("slava@test.com"))
                .extract()
                .path("id");

        given()
                .when()
                .get("http://localhost:8080/users/" + userId)
                .then()
                .statusCode(200)
                .body("id", equalTo(userId))
                .body("name", equalTo("Slava"))
                .body("email", equalTo("slava@test.com"));

        given()
                .when()
                .delete("http://localhost:8080/users/" + userId)
                .then()
                .statusCode(204);

        given()
                .when()
                .get("http://localhost:8080/users/" + userId)
                .then()
                .statusCode(404);
    }
}
