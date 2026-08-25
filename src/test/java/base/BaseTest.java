package base;

import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;

import static io.restassured.RestAssured.given;

public class BaseTest {
    protected static RequestSpecification requestSpec;

    @BeforeAll
    public static void setup(){
        requestSpec = given()
                .contentType("application/json")
                .baseUri("http://localhost:8080");
    }
}
