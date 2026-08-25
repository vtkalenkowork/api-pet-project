package tests;

import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;

public class UserApiTests1 {

    //Common infrastructure code will be moved to separate class in next iteration
    static RequestSpecification requestSpec;

    @BeforeAll
    public static void setup(){
        requestSpec = given()
                .contentType("application/json")
                .baseUri("http://localhost:8080");
    }

    @Test
    public void shouldReturnAllUsers() {
        given(requestSpec)
                .when()
                .get("/users")
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
        given(requestSpec)
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
                .contentType("application/json")
                .body("id", notNullValue())
                .body("name", equalTo("Slava"))
                .body("email", equalTo("slava@test.com"));
    }

    @Test
    public void shouldCreateUserAndCheckItExists() {
        Number userId = given(requestSpec)
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
                .contentType("application/json")
                .body("id", notNullValue())
                .body("name", equalTo("Slava"))
                .body("email", equalTo("slava@test.com"))
                .extract()
                .path("id");

        given(requestSpec)
                .when()
                .get("/users/" + userId)
                .then()
                .statusCode(200)
                .body("id", equalTo(userId))
                .body("name", equalTo("Slava"))
                .body("email", equalTo("slava@test.com"));
    }

    @Test
    public void shouldCheckFullUserFlow() {
        Number userId = given(requestSpec)
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
                .contentType("application/json")
                .body("id", notNullValue())
                .body("name", equalTo("Slava"))
                .body("email", equalTo("slava@test.com"))
                .extract()
                .path("id");

        given(requestSpec)
                .when()
                .get("/users/" + userId)
                .then()
                .statusCode(200)
                .body("id", equalTo(userId))
                .body("name", equalTo("Slava"))
                .body("email", equalTo("slava@test.com"));

        given(requestSpec)
                .when()
                .delete("/users/" + userId)
                .then()
                .statusCode(204);

        given(requestSpec)
                .when()
                .get("/users/" + userId)
                .then()
                .statusCode(404);
    }
}
