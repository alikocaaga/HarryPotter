package com.automation.Test;
import com.automation.utilities.*;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.BeforeAll;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.hamcrest.Matchers.*;
import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;

public class tests {

    @BeforeAll
    public static void setup(){
        baseURI = ConfigurationReader.getProperty("harryPotter_api");

    }
    @Test
    @DisplayName("Verify sorting hat")
    public void test1 (){
        List<String> list = new ArrayList<>(Arrays.asList("\"Gryffindor\"", "\"Ravenclaw\"", "\"Slytherin\"", "\"Hufflepuff\""));
        Response response = given()
                .accept(ContentType.JSON)
                .when()
                .queryParam("key","$2a$10$caBzUc5vMT9z/lM8ruUsXOnqw1vU3oFUxB7qZcfhgmLpS26X9h0Cq")
                .get("/sortingHat").prettyPeek();

        response.then()
                .assertThat().statusCode(200)
                .assertThat().contentType(ContentType.JSON);
                assertTrue(list.contains((response.body().asString())));

    }
@Test
    @DisplayName("Verify bad key" )
    public void test2 (){

       given().accept(ContentType.JSON)
                .when()
                .queryParam("key", "sfbsfbsbsbsb")
                .get("/characters").prettyPeek()
        .then()
                .assertThat()
                .statusCode(401)
                .contentType("application/json; charset=utf-8")
                .statusLine(containsString("Unauthorized"))
                .body("error", is ("API Key Not Found"));

}

@Test
    @DisplayName("Verify no key")

    public void test3 (){

        given()
                .accept(ContentType.JSON)
                .when()
                .get("/characters").prettyPeek()

                .then()
                .assertThat().statusCode(409)
                .contentType(ContentType.JSON)
                .statusLine(containsString("Conflict"))
                .body("error", is("Must pass API key for request"));

}
}
