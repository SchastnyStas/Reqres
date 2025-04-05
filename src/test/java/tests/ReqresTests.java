package tests;

import com.google.gson.Gson;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import reqres_objects.User;
import reqres_objects.UserList;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class ReqresTests {

    public static final String BASE_URL = "https://reqres.in/api/";
    SoftAssert softAssert = new SoftAssert();

    @Test()
    public void postCreateUserTest() {

        given()
                .log().all()
                .body("{\n" +
                        "    \"name\": \"morpheus\",\n" +
                        "    \"job\": \"leader\"\n" +
                        "}\n")
                .header("Content-type", "application/json")
                .log().all()
                .when()
                .post(BASE_URL + "users")
                .then()
                .log().all()
                .body("name", equalTo("morpheus"),
                        "job", equalTo("leader"))

                .statusCode(201);
    }

    @Test(description = "Create User, check status code, 'name' field, 'job' field")
    public void postCreateUserTest1() {
        User user = User.builder()
                .name("morpheus")
                .job("leader")
                .build();

        given()
                .log().all()
                .body(user)
                .header("Content-type", "application/json")
                .log().all()
                .when()
                .post(BASE_URL + "users")
                .then()
                .log().all()
                .body("name", equalTo("morpheus"),
                        "job", equalTo("leader"))
                .statusCode(201);
    }

    @Test(description = "Get list of users and check status code and all exists fields of the " +
            "first user")
    public void getUsersListTest() {

        String body =
                given()
                        .log().all()
                .when()
                        .get(BASE_URL + "users?page=2")
                .then()
                        .log().all()
                        .statusCode(200)
                        .extract().body().asString();

        UserList userList = new Gson().fromJson(body, UserList.class);

        softAssert.assertEquals(userList.getData().get(0).getId(), 7);
        softAssert.assertEquals(userList.getData().get(0).getFirstName(), "Michael");
        softAssert.assertEquals(userList.getData().get(0).getLastName(),
                "Lawson");
        softAssert.assertEquals(userList.getData().get(0).getEmail(),
                "michael.lawson@reqres.in");
        softAssert.assertEquals(userList.getData().get(0).getAvatar(), "https://reqres.in/img/faces/7-image.jpg");
        softAssert.assertAll();
    }
}
