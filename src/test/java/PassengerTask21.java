import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.apache.http.HttpStatus;
import org.example.dto.PassengerRequest;
import org.example.dto.PassengerResponse;
import org.example.testservice.TestServiceApi;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;

@Epic("REST API Tests Epic")
@Feature("Correct data is returned to passenger API requests")
public class PassengerTask21 {

    static TestServiceApi testServiceApi;

    private PassengerRequest newPassengerData() {
        return PassengerRequest.builder().
                name("Vince Noir").
                trips(22).
                airline(5).build();
    }

    private PassengerRequest changedPassengerData() {
        return PassengerRequest.builder().
                name("Howard Moon").
                trips(1).
                airline(1).build();
    }

    @BeforeAll
    public static void beforeAll() {
        testServiceApi = new TestServiceApi();
    }

    @Test
    @Story("Create a passenger")
    @Description("Create a passenger and check the name, trips and airline")
    void createPassengerTest() {
        PassengerRequest passenger = newPassengerData();

        PassengerResponse response = testServiceApi.createPassenger(passenger).then().statusCode(HttpStatus.SC_OK).
                extract().response().as(PassengerResponse.class);

        assertThat(response.getId()).isNotEmpty();

        try {
            assertThat(response.getName()).isEqualTo(passenger.getName());
            assertThat(response.getTrips()).isEqualTo(passenger.getTrips());
            assertThat(response.getAirline().get(0).getId()).isEqualTo(passenger.getAirline());
        } finally {
            testServiceApi.deletePassengerById(response.getId()).then().statusCode(HttpStatus.SC_OK);
        }
    }

    @Test
    @Story("Get a passenger by ID")
    @Description("Get a passenger by ID, check the name, trips and airline")
    void getPassengerByIdTest() {

        PassengerRequest passenger = newPassengerData();

        PassengerResponse response1 = testServiceApi.createPassenger(passenger).then().statusCode(HttpStatus.SC_OK).
                extract().response().as(PassengerResponse.class);

        PassengerResponse response2 = testServiceApi.getPassengerById(response1.getId()).then().statusCode(HttpStatus.SC_OK).
                extract().response().as(PassengerResponse.class);

        assertThat(response2.getId()).isNotEmpty();

        try {
            assertThat(response2.getName()).isEqualTo(passenger.getName());
            assertThat(response2.getTrips()).isEqualTo(passenger.getTrips());
            assertThat(response2.getAirline().get(0).getId()).isEqualTo(passenger.getAirline());
        } finally {
            testServiceApi.deletePassengerById(response2.getId()).then().statusCode(HttpStatus.SC_OK);
        }
    }

    @Test
    @Story("Change passenger data")
    @Description("Change passenger data and check the name, trips and airline")
    void changePassengerTest() {
        PassengerRequest originalPassenger = newPassengerData();

        PassengerResponse response1 = testServiceApi.createPassenger(originalPassenger).then().statusCode(HttpStatus.SC_OK).
                extract().response().as(PassengerResponse.class);

        PassengerRequest changedPassenger = changedPassengerData();

        testServiceApi.putPassenger(response1.getId(), changedPassenger).then().statusCode(HttpStatus.SC_OK);

        PassengerResponse response2 = testServiceApi.getPassengerById(response1.getId()).then().statusCode(HttpStatus.SC_OK).
                extract().response().as(PassengerResponse.class);

        assertThat(response2.getId()).isNotEmpty();

        try {
            assertThat(response2.getName()).isEqualTo(changedPassenger.getName());
            assertThat(response2.getTrips()).isEqualTo(changedPassenger.getTrips());
            assertThat(response2.getAirline().get(0).getId()).isEqualTo(changedPassenger.getAirline());
        } finally {
            testServiceApi.deletePassengerById(response2.getId()).then().statusCode(HttpStatus.SC_OK);
        }
    }

    @Test
    @Story("Delete passenger data")
    @Description("Delete passenger data and check the error message")
    void deletePassengerTest() {
        PassengerRequest passenger = newPassengerData();

        PassengerResponse response1 = testServiceApi.createPassenger(passenger).then().statusCode(HttpStatus.SC_OK).
                extract().response().as(PassengerResponse.class);

        testServiceApi.deletePassengerById(response1.getId()).then().statusCode(HttpStatus.SC_OK).
                assertThat().body("message", is("Passenger data deleted successfully."));
    }

    @Test
    @Story("Can not get deleted passenger data")
    @Description("Delete passenger data and check that passenger data is deleted")
    void getDeletedPassengerTest() {
        PassengerRequest passenger = newPassengerData();

        PassengerResponse response1 = testServiceApi.createPassenger(passenger).then().statusCode(HttpStatus.SC_OK).
                extract().response().as(PassengerResponse.class);

        testServiceApi.deletePassengerById(response1.getId()).then().statusCode(HttpStatus.SC_OK);

        testServiceApi.getPassengerById(response1.getId()).then().statusCode(HttpStatus.SC_NO_CONTENT);
    }

    @Test
    @Story("Put a passenger with wrong ID")
    @Description("Put a passenger with wrong ID and check the error message")
    void putWrongPassengerIDTest() {
        PassengerRequest passenger = newPassengerData();

        testServiceApi.putPassenger("this_ID_is_wrong", passenger).then().statusCode(HttpStatus.SC_BAD_REQUEST).
                assertThat().body("message", is("valid passenger id must submit."));
    }
}
