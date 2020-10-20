package testcases;

import io.restassured.response.Response;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Title;
import models.usermanagement.Login;
import models.usermanagement.Role;

import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import utils.ApplicationConfiguration;
import utils.BaseTest;

import java.io.IOException;
import java.util.Collections;

import static constants.Constants.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.apache.http.HttpStatus.*;


@RunWith(SerenityRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RoleControllerTest extends BaseTest {


    protected static final String USERNAME = ApplicationConfiguration.getUSERNAME();
    protected static final String PASSWORD = ApplicationConfiguration.getPASSWORD();
    public static boolean isPreviousTestPass;



    @Test
    @Title("Post Application Login")
    public void postLogin() {
        Login login = new Login(USERNAME, PASSWORD);
        given().
                spec(requestSpec).
                and().
                body(login).
                when().
                post("/login").
                then().
                assertThat().
                body("username", equalTo("admininventa")).
                statusCode(SC_OK);
    }


    @Test
    @Title("Post Create Role")
    public void testA_postCreateRole() throws IOException {
        String permissionId;
        permissionId = getIdFromPermissionURL(GET_PERMISSION_ID);
        isPreviousTestPass = false;
        Role role = new Role("Automation_Role_" + value + "1", "This is Test Role Created By new Regression Script", true, "Automation Script", Collections.singletonList(permissionId));
        Response response = given().
                spec(requestSpec).
                and().
                body(role).
                when().
                post(ROLE_ENDPOINT + CREATE_ROLE);

        if (response.getStatusCode() == SC_OK)
            isPreviousTestPass = true;
        response.
                then().
                spec(responseSpec);
    }

    @Test
    @Title("Get Role Details By Id")
    public void testB_getRoleDetails() throws IOException {
        String roleId;
        roleId = getIdFromURL(GET_ROLE_ID);

        Assume.assumeTrue(isPreviousTestPass == true);
        isPreviousTestPass = false;
        Response response = given().
                spec(requestSpec).
                when().
                get(ROLE_ENDPOINT + ROLE_DETAILS + roleId);
        if (response.getStatusCode() == SC_OK)
            isPreviousTestPass = true;
        response.
                then().
                spec(responseSpec).
                and().
                body("data._id", equalTo(roleId));

    }

    @Test
    @Title("Put Update Role By Id")
    public void testC_putUpdateRole() throws IOException {
        String currentRoleId;
        String permissionId;

        currentRoleId = getIdFromURL(PUT_ROLE_ID);
        permissionId = getIdFromPermissionURL(GET_PERMISSION_ID);

        Assume.assumeTrue(isPreviousTestPass == true);
        isPreviousTestPass = false;
        Role role = new Role("Update_Automation_Role_" + value + "1", "Updated By Regression new Script", true, "Automation Script", Collections.singletonList(permissionId), currentRoleId);
        Response response = given().
                spec(requestSpec).
                and().
                body(role).
                when().
                put(ROLE_ENDPOINT + UPDATE_ROLE + currentRoleId);
        if (response.getStatusCode() == SC_OK)
            isPreviousTestPass = true;
        response.
                then().
                spec(responseSpec);
    }
    @Test
    @Title("Delete Role")
    public void testD_deleteRole() throws IOException {
        String currentDeleteRoleId;
        currentDeleteRoleId = getIdFromURL(DELETE_ROLE_ID);

        Assume.assumeTrue(isPreviousTestPass == true);
        isPreviousTestPass = false;
        Response response = given().
                spec(requestSpec).
                when().
                delete(ROLE_ENDPOINT + DELETE_ROLE + currentDeleteRoleId);

        if (response.getStatusCode() == SC_OK)
            isPreviousTestPass = true;
        response.
                then().
                assertThat().
                statusCode(SC_OK);
//                and().
//                body("data.message", equalTo("Delete Success"));

    }

    @Test
    @Title("Get All Role List")
    public void testE_getAllRole() {
        given().
                spec(requestSpec).
                when().
                get(ROLE_ENDPOINT + GET_ALL_ROLE).
                then().
                spec(responseSpec);
    }


}
