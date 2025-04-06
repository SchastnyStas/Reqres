package tests;

import com.google.gson.Gson;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import reqres_objects.resource.ResourceData;
import reqres_objects.resource.ResourceForResourcesList;
import reqres_objects.resource.ResourcesList;
import reqres_objects.user.User;
import reqres_objects.user.UserData;
import reqres_objects.user.UserList;

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

    @Test(description = "Get list of users and check status code and all existing fields of the " +
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

    @Test(description = "Get a single user and check the status code and all existing user fields")
    public void getSingleUserTest() {
        String data =
                given()
                        .log().all()
                        .when()
                        .get(BASE_URL + "users/2")
                        .then()
                        .log().all()
                        .statusCode(200)
                        .extract().body().asString();

        UserData user = new Gson().fromJson(data, UserData.class);
        softAssert.assertEquals(user.getData().getFirstName(), "Janet");
        softAssert.assertEquals(user.getData().getLastName(), "Weaver");
        softAssert.assertAll();
    }

    @Test(description = "Get information that a single user is empty")
    public void getSingleUserNotFoundTest() {

        given()
                .log().all()
                .when()
                .get(BASE_URL + "users/23")
                .then()
                .log().all()
                .statusCode(404);
    }

    @Test(description = "Get resources list and check the status code and all existing resource " +
            "fields")
    public void getListResourceTest() {

        ResourceForResourcesList expectedResource = new ResourceForResourcesList(1, "cerulean",
                2000, "#98B2D1", "15-4020");

        String data =
                given()
                        .log().all()
                        .when()
                        .get(BASE_URL + "unknown")
                        .then()
                        .log().all()
                        .statusCode(200)
                        .extract().body().asString();

        ResourcesList resourcesList = new Gson().fromJson(data, ResourcesList.class);

        Assert.assertEquals(resourcesList.getData().get(0), expectedResource);
    }

    @Test(description = "Get a single resource and check the status code and all existing " +
            "resource fields")
    public void getSingleResourceTest() {

        ResourceForResourcesList expectedResource = new ResourceForResourcesList(2, "fuchsia rose",
                2001, "#C74375", "17-2031");

        String data =
                given()
                        .log().all()
                        .when()
                        .get(BASE_URL + "unknown/2")
                        .then()
                        .log().all()
                        .statusCode(200)
                        .extract().body().asString();

        ResourceData resource = new Gson().fromJson(data, ResourceData.class);
        Assert.assertEquals(resource.getData(), expectedResource);
    }

    @Test(description = "Update username and job")
    public void putUserTest() {
        User user = User.builder()
                .name("morpheus")
                .job("zion resident")
                .build();

        given()
                .log().all()
                .body(user)
                .header("Content-type", "application/json")
                .log().all()
                .when()
                .put(BASE_URL + "users/2")
                .then()
                .log().all()
                .body("name", equalTo("morpheus"),
                        "job", equalTo("zion resident"))
                .statusCode(200);
    }

    @Test(description = "Update username and job")
    public void patchUserTest() {
        User user = User.builder()
                .name("morpheus")
                .job("zion resident")
                .build();

        given()
                .log().all()
                .body(user)
                .header("Content-type", "application/json")
                .log().all()
                .when()
                .patch(BASE_URL + "users/2")
                .then()
                .log().all()
                .body("name", equalTo("morpheus"),
                        "job", equalTo("zion resident"))
                .statusCode(200);
    }

    @Test(description = "Delete test")
    public void deleteTest() {

        given()
                .log().all()
                .when()
                .delete(BASE_URL + "users/2")
                .then()
                .log().all()
                .statusCode(204);
    }

    @Test(description = "Successful registration")
    public void postSuccessfulRegisterTest() {
        User user = User.builder()
                .email("eve.holt@reqres.in")
                .password("pistol")
                .build();

        given()
                .log().all()
                .body(user)
                .header("Content-type", "application/json")
                .log().all()
                .when()
                .post(BASE_URL + "register")
                .then()
                .log().all()
                .body("id", equalTo(4), "token", equalTo("QpwL5tke4Pnpja7X4"))
                .statusCode(200);
    }

    @Test(description = "Unsuccessful registration")
    public void postUnsuccessfulRegisterTest() {
        User user = User.builder()
                .email("sydney@fife")
                .build();

        given()
                .log().all()
                .body(user)
                .header("Content-type", "application/json")
                .log().all()
                .when()
                .post(BASE_URL + "register")
                .then()
                .log().all()
                .body("error", equalTo("Missing password"))
                .statusCode(400);
    }

    @Test(description = "Successful login")
    public void postSuccessfulLoginTest() {
        User user = User.builder()
                .email("eve.holt@reqres.in")
                .password("cityslicka")
                .build();

        given()
                .log().all()
                .body(user)
                .header("Content-type", "application/json")
                .log().all()
                .when()
                .post(BASE_URL + "login")
                .then()
                .log().all()
                .body("token", equalTo("QpwL5tke4Pnpja7X4"))
                .statusCode(200);
    }

    @Test(description = "Unsuccessful login")
    public void postUnsuccessfulLoginTest() {
        User user = User.builder()
                .email("peter@klaven")
                .build();

        given()
                .log().all()
                .body(user)
                .header("Content-type", "application/json")
                .log().all()
                .when()
                .post(BASE_URL + "register")
                .then()
                .log().all()
                .body("error", equalTo("Missing password"))
                .statusCode(400);
    }

    @Test(description = "Get request after delayed")
    public void getDelayedResponseTest() {

        given()
                .log().all()
                .when()
                .get(BASE_URL + "users?delay=3")
                .then()
                .log().all()
                .statusCode(200);
    }
}