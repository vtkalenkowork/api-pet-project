package config;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class Config {
    private static final String USER_API_BASE_URL = "http://localhost:8080";
    private static final String AUTH_BASE_URL = "http://localhost:8081";

    public static RequestSpecification getUserRequestSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(USER_API_BASE_URL)
                .setContentType(ContentType.JSON)
                .build();
    }

    public static RequestSpecification getAuthRequestSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(AUTH_BASE_URL)
                .setContentType(ContentType.JSON)
                .build();
    }
}
