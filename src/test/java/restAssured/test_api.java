package restAssured;

import com.github.javafaker.Faker;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.assertTrue;

public class test_api {
//    String phone = "+201235887335";
    Faker faker = new Faker();
    String phone = faker.numerify("+2011########");
    String name = faker.name().firstName();

    @Test
    public void register(){
        Map<String, String> headers1 = new HashMap<>();
        headers1.put("DeviceKey","123");
        headers1.put("DeviceType","ios");
        headers1.put("Accept","application/json");
        Response response =
                given()
                        .baseUri("https://laundrydashboard.otloob.net/api/v1")
                        .headers(headers1)
                        .contentType(ContentType.JSON)
                        .body("{\"name\" : \"" + name + "\", \"mobile\" : \"" + phone + "\"}")
                .when()
                        .post("/register");
        if(response.statusCode() != 200) {
            throw new RuntimeException("Something went wrong with this request");
        }
    }
    @Test
    public void verify() {
        int otp = 1000;
        Map<String, String> headers2 = new HashMap<>();
        headers2.put("Accept", "application/json");
        Response response =
                given()
                        .baseUri("https://laundrydashboard.otloob.net/api/v1")
                        .headers(headers2)
                        .contentType(ContentType.JSON)
                        .body("{\"mobile\" : \"" + phone + "\", \"otp\" : \"" + otp + "\"}")
                        .when()
                        .post("/verify-mobile/otp");;
        if (response.statusCode() != 200) {
            throw new RuntimeException("Something went wrong with this request");
        }
        System.out.println(name);
        System.out.println(phone);
        String token = response.jsonPath().getString("data['access']['token']");
        System.out.println(token);
    }
}
