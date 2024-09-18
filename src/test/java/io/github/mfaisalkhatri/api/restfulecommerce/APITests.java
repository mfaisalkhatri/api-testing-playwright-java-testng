package io.github.mfaisalkhatri.api.restfulecommerce;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.RequestOptions;
import io.github.mfaisalkhatri.api.restfulecommerce.testdata.OrderData;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static io.github.mfaisalkhatri.api.restfulecommerce.testdata.OrderDataBuilder.getNewOrder;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class APITests extends BaseTest{

    private List<OrderData> orderList;

    @BeforeClass
    public void testSetup() {
        orderList = new ArrayList<>();
    }

    @Test
    public void testShouldCreateNewOrders() throws JsonProcessingException {

        int totalOrders = 4;

        for (int i = 0; i < totalOrders; i++) {
            orderList.add(getNewOrder());
        }

        APIResponse response = request.post("/addOrder", RequestOptions.create()
                .setData(orderList));
        Helper helper = new Helper(response);
        helper.logResponseDetails();

        final JSONObject responseObject = new JSONObject(response.text());
        final JSONArray ordersArray = responseObject.getJSONArray("orders");

        assertEquals(response.status(), 201);
        assertEquals(responseObject.get("message"), "Orders added successfully!");
        assertNotNull(ordersArray.getJSONObject(0).get("id"));
        assertEquals(orderList.get(0).getUserId(), ordersArray.getJSONObject(0).get("user_id"));
        assertEquals(orderList.get(0).getProductId(), ordersArray.getJSONObject(0).get("product_id"));
        assertEquals(orderList.get(0).getTotalAmt(), ordersArray.getJSONObject(0).get("total_amt"));
    }

    @Test
    public void testShouldGetAllOrders() {

        APIResponse response = request.get("/getAllOrders");

        Helper helper = new Helper(response);
        helper.logResponseDetails();

        final JSONObject responseObject = new JSONObject(response.text());
        final JSONArray ordersArray = responseObject.getJSONArray("orders");

        assertEquals(response.status(), 200);
        assertEquals(responseObject.get("message"), "Orders fetched successfully!");
        assertEquals(orderList.get(0).getUserId(), ordersArray.getJSONObject(0).get("user_id"));
        assertEquals(orderList.get(0).getProductId(), ordersArray.getJSONObject(0).get("product_id"));
        assertEquals(orderList.get(0).getTotalAmt(), ordersArray.getJSONObject(0).get("total_amt"));
    }

    @Test
    public void testShouldGetOrderUsingId() {
        int orderId = 1;
        APIResponse response = request.get("/getOrder", RequestOptions.create().setQueryParam("id", orderId));

        Helper helper = new Helper(response);
        helper.logResponseDetails();

        final JSONObject responseObject = new JSONObject(response.text());
        final JSONArray ordersArray = responseObject.getJSONArray("orders");

        assertEquals(response.status(), 200);
        assertEquals(ordersArray.getJSONObject(0).get("id"), orderId);
        assertEquals(responseObject.get("message"), "Order found!!");

    }

    @Test
    public void testShouldGetOrdersUsingUserId() {
        int userId = 2;

        APIResponse response = request.get("/getOrder", RequestOptions.create().setQueryParam("user_id", userId));

        Helper helper = new Helper(response);
        helper.logResponseDetails();

        final JSONObject responseObject = new JSONObject(response.text());
        final JSONArray ordersArray = responseObject.getJSONArray("orders");

        assertEquals(response.status(), 200);
        assertEquals(responseObject.get("message"), "Order found!!");
        assertEquals(orderList.get(0).getUserId(), ordersArray.getJSONObject(0).get("user_id"));

    }

}
