package org.fun7;

import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.fun7.model.User;
import org.fun7.repository.UserRepository;
import org.fun7.util.TimeUtils;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.persistence.LockModeType;
import java.time.LocalDateTime;
import java.util.HashMap;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

@QuarkusTest
public class CheckServicesControllerTest {

    @InjectMock
    UserRepository userRepository;

    @InjectMock
    TimeUtils timeUtils;

    @Test
    public void testGetCheckServicesNoParams() {
        given()
                .when()
                .get("/check-services")
                .then()
                .statusCode(400)
                .body(containsString("Please provide an user id"),
                        containsString("Please provide a country code"),
                        containsString("Please provide a timezone"));
    }

    @Test
    public void testGetCheckServicesInvalidParameter() {
        given()
                .param("cc", "BRA")
                .param("userId", 1)
                .param("timezone", "Americas/Sao_Paulo")
                .when()
                .get("/check-services")
                .then()
                .statusCode(400)
                .body(containsString("Invalid country code"),
                        containsString("Invalid timezone"));
    }

    @Test
    public void testGetCheckServicesUserNotFound() {
        given()
                .param("cc", "US")
                .param("userId", 1)
                .param("timezone", "America/Sao_Paulo")
                .when()
                .get("/check-services")
                .then()
                .statusCode(404)
                .body(is("User not found"));
    }

    @Test
    @TestTransaction
    public void testGetCheckServicesAllDisabled() {
        var user = new User();
        user.setUserId(1L);
        user.setTimesPlayed(0);
        Mockito.when(userRepository.findById(1L, LockModeType.PESSIMISTIC_WRITE)).thenReturn(user);

        Mockito.when(timeUtils.getDateTimeInLjubljana())
                .thenReturn(LocalDateTime.of(2021, 12, 26, 12, 59, 0));

        given()
                .param("cc", "MX")
                .param("userId", 1)
                .param("timezone", "America/Sao_Paulo")
                .when()
                .get("/check-services")
                .then()
                .statusCode(200)
                .body(is("{\"multiplayer\":\"disabled\",\"userSupport\":\"disabled\",\"ads\":\"disabled\"}"));

    }

    @Test
    @TestTransaction
    public void testGetCheckServicesMultiplayerDisabledNewUser() {

        String dateTimeExpected = "2021-12-27T12:00:00Z";
        var dateTime =
                LocalDateTime.of(2021, 12, 27, 14, 59, 0);

        Mockito.when(timeUtils.getDateTimeInLjubljana()).thenReturn(dateTime);

        var user = new User();
        user.setUserId(1L);
        user.setTimesPlayed(0);
        Mockito.when(userRepository.findById(1L, LockModeType.PESSIMISTIC_WRITE)).thenReturn(user);

        given()
                .param("cc", "US")
                .param("userId", 1)
                .param("timezone", "America/Sao_Paulo")
                .when()
                .get("/check-services")
                .then()
                .statusCode(200)
                .body(is("{\"multiplayer\":\"disabled\",\"userSupport\":\"enabled\",\"ads\":\"enabled\"}"));
    }

    @Test
    @TestTransaction
    public void testGetCheckServicesCustomerSupportDisabled() {

        var params = new HashMap<String, String>();
        params.put("cc", "US");
        params.put("userId", "1");
        params.put("timezone", "America/Sao_Paulo");

        var expectedBody = "{\"multiplayer\":\"disabled\",\"userSupport\":\"disabled\",\"ads\":\"enabled\"}";

        var dateTime =
                LocalDateTime.of(2021, 12, 27, 16, 0, 0);

        Mockito.when(timeUtils.getDateTimeInLjubljana()).thenReturn(dateTime);

        var user = new User();
        user.setUserId(1L);
        user.setTimesPlayed(0);
        Mockito.when(userRepository.findById(1L, LockModeType.PESSIMISTIC_WRITE)).thenReturn(user);

        given()
                .params(params)
                .when().get("/check-services")
                .then()
                .statusCode(200).body(is(expectedBody));

        Mockito.when(timeUtils.getDateTimeInLjubljana()).thenReturn(dateTime.withHour(7));

        given()
                .params(params)
                .when().get("/check-services")
                .then().statusCode(200).body(is(expectedBody));

        Mockito.when(timeUtils.getDateTimeInLjubljana()).thenReturn(dateTime.withHour(12).withDayOfMonth(26));

        given()
                .params(params)
                .when().get("/check-services")
                .then().statusCode(200).body(is(expectedBody));
    }

    @Test
    @TestTransaction
    public void testGetCheckServicesMultiplayerDisabledVeteranPlayerOutsideUS() {
        var user = new User();
        user.setUserId(1L);
        user.setTimesPlayed(6);
        Mockito.when(userRepository.findById(1L, LockModeType.PESSIMISTIC_WRITE)).thenReturn(user);

        Mockito.when(timeUtils.getDateTimeInLjubljana())
                .thenReturn(LocalDateTime.of(2021, 12, 27, 12, 59, 0));

        given()
                .param("cc", "BR")
                .param("userId", 1)
                .param("timezone", "America/Sao_Paulo")
                .when()
                .get("/check-services")
                .then()
                .statusCode(200)
                .body(is("{\"multiplayer\":\"disabled\",\"userSupport\":\"enabled\",\"ads\":\"enabled\"}"));

    }

    @Test
    @TestTransaction
    public void testGetCheckServicesAllEnabled() {
        var user = new User();
        user.setUserId(1L);
        user.setTimesPlayed(6);
        Mockito.when(userRepository.findById(1L, LockModeType.PESSIMISTIC_WRITE)).thenReturn(user);

        Mockito.when(timeUtils.getDateTimeInLjubljana())
                .thenReturn(LocalDateTime.of(2021, 12, 27, 12, 59, 0));

        given()
                .param("cc", "US")
                .param("userId", 1)
                .param("timezone", "America/Sao_Paulo")
                .when()
                .get("/check-services")
                .then()
                .statusCode(200)
                .body(is("{\"multiplayer\":\"enabled\",\"userSupport\":\"enabled\",\"ads\":\"enabled\"}"));

    }

    @Test
    @TestTransaction
    public void testGetCheckServicesAdDisabled500Error() {
        var user = new User();
        user.setUserId(1L);
        user.setTimesPlayed(6);
        Mockito.when(userRepository.findById(1L, LockModeType.PESSIMISTIC_WRITE)).thenReturn(user);

        Mockito.when(timeUtils.getDateTimeInLjubljana())
                .thenReturn(LocalDateTime.of(2021, 12, 27, 12, 59, 0));

        given()
                .param("cc", "JP")
                .param("userId", 1)
                .param("timezone", "America/Sao_Paulo")
                .when()
                .get("/check-services")
                .then()
                .statusCode(200)
                .body(is("{\"multiplayer\":\"disabled\",\"userSupport\":\"enabled\",\"ads\":\"disabled\"}"));

    }
}
