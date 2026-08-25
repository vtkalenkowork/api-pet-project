package tests;

import base.BaseTest;
import config.Config;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class AuthApiTests1 {

    @Test
    public void shouldReturn401WithInvalidToken(){
        given(Config.getAuthRequestSpec())
                .header("Authorization", "Bearer invalid-token")
                .when()
                .get("/auth/profile")
                .then()
                .statusCode(401);
    }

    @Test
    public void shouldReturn401WithMalformedToken(){
        given(Config.getAuthRequestSpec())
                .header("Authorization", "Bearer abc.def.ghi")
                .when()
                .get("/auth/profile")
                .then()
                .statusCode(401);
    }

    @Test
    public void shouldReturn401WithEmptyHeader(){
        given(Config.getAuthRequestSpec())
                .header("Authorization", "")
                .when()
                .get("/auth/profile")
                .then()
                .statusCode(401);
    }

    @Test
    public void shouldReturn401WithInvalidSchema(){
        given(Config.getAuthRequestSpec())
                .header("Authorization", "Basic abc123")
                .when()
                .get("/auth/profile")
                .then()
                .statusCode(401);
    }

    @Test
    public void shouldReturn401WithoutToken(){
        given(Config.getAuthRequestSpec())
                .header("Authorization", "Bearer")
                .when()
                .get("/auth/profile")
                .then()
                .statusCode(401);
    }
}
