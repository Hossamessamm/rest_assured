package restAssured;

import com.github.javafaker.Faker;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertTrue;

public class TestApi {

    Faker faker = new Faker();
    String num = faker.regexify("(+201|01|00201)[0125]{1}[0-9]{8}");
    String name1 = faker.name().firstName();
    String token;
    Map<String, String> headers2 = new HashMap<>();

    @Test
    public void Register(){
        Map<String, String> headers1 = new HashMap<>();
        headers1.put("DeviceKey","123");
        headers1.put("DeviceType","ios");
        headers1.put("Accept","application/json");
        Response response =
                given()
                        .baseUri("https://laundrydashboard.otloob.net/api/v1")
                        .headers(headers1)
                        .contentType(ContentType.JSON)
                        .body("{\"name\" : \"" + name1 + "\", \"mobile\" : \"" + num + "\"}")
                .when()
                        .post("/register");
        if(response.statusCode() != 200) {
            throw new RuntimeException("Something went wrong with this request");
        }
    }

    @Test(priority = 1)
    public void Verify() {
        int otp = 1000;
        headers2.put("Accept", "application/json");
        Response response =
                given()
                        .baseUri("https://laundrydashboard.otloob.net/api/v1")
                        .headers(headers2)
                        .contentType(ContentType.JSON)
                        .body("{\"mobile\" : \"" + num + "\", \"otp\" : \"" + otp + "\"}")
                .when()
                        .post("/verify-mobile/otp");
        if (response.statusCode() != 200) {
            throw new RuntimeException("Something went wrong with this request");
        }
        token = response.jsonPath().getString("data['access']['token']");
        System.out.println(token);
    }

    @Test(priority = 2)
    public void GetProfileInfo()
    {
        Response response =
                given()
                        .auth().oauth2(token)
                        .baseUri("https://laundrydashboard.otloob.net/api/v1")
                        .contentType(ContentType.JSON)
                        .when()
                        .get("/customer/profile").then().extract().response();
        String name = response.jsonPath().getString("data['customer']['user']['name']");
        String mobilePhone = response.jsonPath().getString("data['customer']['user']['mobile']");
        System.out.println(name);
        System.out.println(mobilePhone);
    }


    @Test(enabled = false,priority = 3)
    public void Logout()
    {
        Map<String, String> headers2 = new HashMap<>();
        headers2.put("Accept", "application/json");
        headers2.put("DeviceKey","123");
        headers2.put("DeviceType","ios");
        Response response =
                given()
                        .auth().oauth2(token)
                        .baseUri("https://laundrydashboard.otloob.net/api/v1")
                        .headers(headers2)
                        .contentType(ContentType.JSON)
                        .when()
                        .post("/customer/logout");
        if (response.statusCode() != 200) {
            throw new RuntimeException("Something went wrong with this request");
        }
    }
}
