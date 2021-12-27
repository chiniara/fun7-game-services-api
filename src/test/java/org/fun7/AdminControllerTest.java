package org.fun7;

import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AdminControllerTest {

    @Test
    @Order(1)
    public void testGetEmptyUsers() {
        given()
                .when().get("/admin/user")
                .then().statusCode(200).body(is("[]"));
    }

    @Test
    @Order(2)
    @TestTransaction
    public void addOneUser() {
        given()
                .body("{}")
                .contentType("application/json")
                .when().post("/admin/user")
                .then().statusCode(201);
    }

    @Test
    @Order(3)
    public void testGetListUsers() {
        given()
                .when().get("/admin/user")
                .then().statusCode(200).body(is("[{\"userId\":1,\"timesPlayed\":0}]"));
    }

    @Test
    @Order(4)
    public void testGetOneUser() {
        given()
                .when().get("/admin/user/1")
                .then().statusCode(200).body(is("{\"userId\":1,\"timesPlayed\":0}"));
    }

    @Test
    @Order(5)
    public void testDeleteUser() {
        given()
                .when().delete("/admin/user/1")
                .then().statusCode(200);

        given()
                .when().get("/admin/user")
                .then().statusCode(200).body(is("[]"));
    }

    @Test
    @Order(6)
    public void testDeleteUserButNotFound() {
        given()
                .when().delete("/admin/user/1")
                .then().statusCode(404);

    }

    @Test
    @Order(7)
    public void testGetOneUserButNotFound() {
        given()
                .when().get("/admin/user/1")
                .then().statusCode(404);
    }


}
