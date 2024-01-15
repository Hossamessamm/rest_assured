package restAssured;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class test_api {
    Map<String, String> headers = new HashMap<>();


//    String name = "Tessst";
    String phone = "+201234565555";
    int otp = 1000;
    @Test
    public void register()
    {
//        headers.put("DeviceKey","123");
//        headers.put("DeviceType","ios");
        headers.put("Accept","application/json");
        Response response =
                given()
                        .baseUri("https://laundrydashboard.otloob.net/api/v1")
                        .headers(headers)
                        .contentType(ContentType.JSON)
                        .body("{\"mobile\" : \"" + phone + "\", \"otp\" : \"" + otp + "\"}")
                        .log().all()
                .when()
                        .post("/verify-mobile/otp")
                .then()
                        .log().all()
                        .extract().response();

        if(response.statusCode() != 200) {
            throw new RuntimeException("Something went wrong with this request");
        }

        String token = response.path("data['access'][token]");
        System.out.println(token);
//        System.out.println(name);
//        accessToken = response.path("access['token']]");
//        userID = response.path("userID");
//        name = response.path("data['user'][first_name]");
//
//        System.out.println(name);
//        System.out.println(accessToken);
    }
}
