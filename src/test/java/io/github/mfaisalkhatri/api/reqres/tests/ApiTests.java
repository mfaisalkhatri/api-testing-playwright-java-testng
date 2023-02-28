package io.github.mfaisalkhatri.api.reqres.tests;

import static io.github.mfaisalkhatri.api.reqres.data.EmployeeDataBuilder.getEmployeeData;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.RequestOptions;
import io.github.mfaisalkhatri.api.reqres.data.EmployeeData;
import org.json.JSONObject;
import org.testng.annotations.Test;

/**
 * @author Faisal Khatri
 * @since 2/28/2023
 **/
public class ApiTests extends BaseTest {
    @Test
    public void testGetAPI () {
        APIResponse response = manager.getRequest ("/api/users/4");
        assertEquals (response.status (), 200);

        JSONObject jsonObject = new JSONObject (response.text ());
        JSONObject dataObject = jsonObject.getJSONObject ("data");

        assertEquals (dataObject.get ("email")
            .toString (), "eve.holt@reqres.in");
        assertEquals (dataObject.get ("first_name")
            .toString (), "Eve");
    }

    @Test
    public void testPostAPI () {
        EmployeeData employeeData = getEmployeeData ();
        APIResponse response = manager.postRequest ("/api/users", RequestOptions.create ()
            .setData (employeeData));
        assertEquals (response.status (), 201);

        JSONObject jsonObject = new JSONObject (response.text ());
        assertNotNull (jsonObject.get ("id"));
        assertEquals (jsonObject.get ("name"), employeeData.getName ());
        assertEquals (jsonObject.get ("job"), employeeData.getJob ());
    }

    @Test
    public void testPutAPI () {
        EmployeeData employeeData = getEmployeeData ();
        APIResponse response = manager.putRequest ("/api/users/2", RequestOptions.create ()
            .setData (employeeData));
        assertEquals (response.status (), 200);

        JSONObject jsonObject = new JSONObject (response.text ());
        assertNotNull (jsonObject.get ("updatedAt"));
        assertEquals (jsonObject.get ("name"), employeeData.getName ());
        assertEquals (jsonObject.get ("job"), employeeData.getJob ());
    }

    @Test
    public void testPatchAPI () {
        EmployeeData employeeData = getEmployeeData ();
        APIResponse response = manager.patchRequest ("/api/users/2", RequestOptions.create ()
            .setData (employeeData));
        assertEquals (response.status (), 200);

        JSONObject jsonObject = new JSONObject (response.text ());
        assertNotNull (jsonObject.get ("updatedAt"));
        assertEquals (jsonObject.get ("name"), employeeData.getName ());
        assertEquals (jsonObject.get ("job"), employeeData.getJob ());
    }

    @Test
    public void testDeleteAPI () {
        EmployeeData employeeData = getEmployeeData ();
        APIResponse response = manager.deleteRequest ("/api/users/2", RequestOptions.create ()
            .setData (employeeData));
        assertEquals (response.status (), 204);
    }

}
