package io.github.mfaisalkhatri.api.reqres.tests;

import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.RequestOptions;
import io.github.mfaisalkhatri.api.reqres.data.EmployeeData;
import org.json.JSONObject;
import org.testng.annotations.Test;

import static io.github.mfaisalkhatri.api.reqres.data.EmployeeDataBuilder.getEmployeeData;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * @author Faisal Khatri
 * @since 2/28/2023
 **/
public class ApiTests extends BaseTest {
    @Test
    public void testGetAPI() {
        final APIResponse response = this.manager.getRequest("/api/users/4");
        assertEquals(response.status(), 200);

        final JSONObject jsonObject = new JSONObject(response.text());
        final JSONObject dataObject = jsonObject.getJSONObject("data");

        assertEquals(dataObject.get("email")
                .toString(), "eve.holt@reqres.in");
        assertEquals(dataObject.get("first_name")
                .toString(), "Eve");
    }

    @Test
    public void testPostAPI() {
        final EmployeeData employeeData = getEmployeeData();
        final APIResponse response = this.manager.postRequest("/api/users", RequestOptions.create()
                .setData(employeeData));
        assertEquals(response.status(), 201);

        final JSONObject jsonObject = new JSONObject(response.text());
        assertNotNull(jsonObject.get("id"));
        assertEquals(jsonObject.get("name"), employeeData.getName());
        assertEquals(jsonObject.get("job"), employeeData.getJob());
    }

    @Test
    public void testPutAPI() {
        final EmployeeData employeeData = getEmployeeData();
        final APIResponse response = this.manager.putRequest("/api/users/2", RequestOptions.create()
                .setData(employeeData));
        assertEquals(response.status(), 200);

        final JSONObject jsonObject = new JSONObject(response.text());
        assertNotNull(jsonObject.get("updatedAt"));
        assertEquals(jsonObject.get("name"), employeeData.getName());
        assertEquals(jsonObject.get("job"), employeeData.getJob());
    }

    @Test
    public void testPatchAPI() {
        final EmployeeData employeeData = getEmployeeData();
        final APIResponse response = this.manager.patchRequest("/api/users/2", RequestOptions.create()
                .setData(employeeData));
        assertEquals(response.status(), 200);

        final JSONObject jsonObject = new JSONObject(response.text());
        assertNotNull(jsonObject.get("updatedAt"));
        assertEquals(jsonObject.get("name"), employeeData.getName());
        assertEquals(jsonObject.get("job"), employeeData.getJob());
    }

    @Test
    public void testDeleteAPI() {
        final EmployeeData employeeData = getEmployeeData();
        final APIResponse response = this.manager.deleteRequest("/api/users/2", RequestOptions.create()
                .setData(employeeData));
        assertEquals(response.status(), 204);
    }

}
