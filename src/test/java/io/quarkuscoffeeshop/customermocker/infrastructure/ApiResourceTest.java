package io.quarkuscoffeeshop.customermocker.infrastructure;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.restassured.RestAssured;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class ApiResourceTest {

    @Test
    public void testStartApplication() {
        RestAssured.given()
                .when().post("/api/start")
                .then()
                .statusCode(200);

        RestAssured.given()
                .when().get("/api/running")
                .then()
                .statusCode(200)
                .body(CoreMatchers.is("true"));
    }

    @Test
    public void testStopApplication() {
        RestAssured.given()
                .when().post("/api/stop")
                .then()
                .statusCode(200);

        RestAssured.given()
                .when().get("/api/running")
                .then()
                .statusCode(200)
                .body(CoreMatchers.is("false"));
    }
}
