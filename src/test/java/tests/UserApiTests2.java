package tests;

import base.BaseTest;
import data.UserRequest;
import data.UserResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserApiTests2 extends BaseTest {
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

    @Test
    public void shouldCreateAndUpdateUser() {
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
                .extract()
                .path("id");

        given(requestSpec)
                .body("""
                        {
                        "name": "Viacheslav",
                        "email": "viacheslav@test.com"
                        }
                        """)
                .when()
                .put("/users/" + userId)
                .then()
                .statusCode(200)
                .contentType("application/json")
                .body("id", equalTo(userId))
                .body("name", equalTo("Viacheslav"))
                .body("email", equalTo("viacheslav@test.com"));

        given(requestSpec)
                .when()
                .get("/users/" + userId)
                .then()
                .statusCode(200)
                .body("id", equalTo(userId))
                .body("name", equalTo("Viacheslav"))
                .body("email", equalTo("viacheslav@test.com"));
    }

    @Test
    public void shouldReturnErrorWithEmptyName() {
        given(requestSpec)
                .body("""
                        {
                        "name" : "",
                        "email" : "test@test.com"
                        }
                        """)
                .when()
                .post("/users")
                .then()
                .statusCode(400)
                .body("errors", contains("Name cannot be empty"));
    }

    //Starting to use formatted data
    @Test
    public void shouldCreateFormatedUser() {
        String name = "Slava";
        String email = "slava@test.com";

        given(requestSpec)
                .body("""
                        {
                        "name" : "%s",
                        "email" : "%s"
                        }
                        """
                        .formatted(name, email))
                .when()
                .post("/users")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("name", equalTo(name))
                .body("email", equalTo(email));
    }

    //Starting to use POJO/DTO
    @Test
    public void shouldCreateUserWithPojo() {
        UserRequest user = new UserRequest("Slava", "slava@test.com");

        given(requestSpec)
                .body("""
                        {
                        "name" : "%s",
                        "email" : "%s"
                        }
                        """
                        .formatted(user.getName(), user.getEmail()))
                .when()
                .post("/users")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("name", equalTo(user.getName()))
                .body("email", equalTo(user.getEmail()));
    }

    //Starting to use serialization
    @Test
    public void shouldCreateSerializedUser(){
        UserRequest user = new UserRequest("Slava", "slava@test.com");

        given(requestSpec)
                .body(user)
                .when()
                .post("/users")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("name", equalTo(user.getName()))
                .body("email", equalTo(user.getEmail()));
    }

    //Starting to use deserialization abd DTO
    @Test
    public void shouldCreateAndDeserializeUser(){
        UserRequest user = new UserRequest("Slava", "slava@test.com");

        Response response = given(requestSpec)
                .body(user)
                .when()
                .post("/users")
                .then()
                .statusCode(201)
                .extract()
                .response();

        UserResponse createdUser = response.body().as(UserResponse.class);

        assertNotNull(createdUser.getId());
        assertEquals(user.getName(), createdUser.getName());
        assertEquals(user.getEmail(), createdUser.getEmail());
    }
}
