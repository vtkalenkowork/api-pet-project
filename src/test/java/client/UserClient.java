package client;

import config.Config;
import data.UserRequest;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class UserClient {

    public Response createUser(UserRequest user) {
        return given(Config.getUserRequestSpec())
                .body(user)
                .when()
                .post("/users")
                .then()
                .extract()
                .response();
    }

    public Response getUser(Long userId){
        return given(Config.getUserRequestSpec())
                .when()
                .get("/users/" + userId)
                .then()
                .extract()
                .response();
    }

    public Response getAllUsers(){
        return given(Config.getUserRequestSpec())
                .when()
                .get("/users")
                .then()
                .extract()
                .response();
    }

    public Response updateUser(UserRequest user, Long userId){
        return given(Config.getUserRequestSpec())
                .body(user)
                .when()
                .put("/users/" + userId)
                .then()
                .extract()
                .response();
    }

    public Response deleteUser(Long userId){
        return given(Config.getUserRequestSpec())
                .when()
                .delete("/users/" + userId)
                .then()
                .extract()
                .response();
    }

    public Response getUsersByName(String name){
        return given(Config.getUserRequestSpec())
                .queryParam("name", name)
                .when()
                .get("/users")
                .then()
                .extract()
                .response();
    }

}
