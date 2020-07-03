package nl.sander;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class BewegingResourceTest {

    @Test
    public void testHelloEndpoint() {
        given()
          .when().get("/beweging/all")
          .then()
             .statusCode(200);
    }

}