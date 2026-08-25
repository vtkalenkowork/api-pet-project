package client;

import config.Config;
import data.LoginRequest;
import data.UserRequest;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class AuthClient {
    public Response login(LoginRequest loginRequest) {
        return given(Config.getAuthRequestSpec())
                .body(loginRequest)
                .when()
                .post("/auth/login")
                .then()
                .extract()
                .response();
    }
}
