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
import static io.github.mfaisalkhatri.api.restfulecommerce.testdata.OrderDataBuilder.getUpdatedOrder;
import static io.github.mfaisalkhatri.api.restfulecommerce.testdata.TokenBuilder.getCredentials;
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
    public void testShouldGetOrderUsingOrderId() {
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
        String userId = "2";

        APIResponse response = request.get("/getOrder", RequestOptions.create().setQueryParam("user_id", userId));

        Helper helper = new Helper(response);
        helper.logResponseDetails();

        final JSONObject responseObject = new JSONObject(response.text());
        final JSONArray ordersArray = responseObject.getJSONArray("orders");

        assertEquals(response.status(), 200);
        assertEquals(responseObject.get("message"), "Order found!!");
        assertEquals(ordersArray.getJSONObject(0).get("user_id"), userId);

    }

    @Test
    public void testShouldGetOrdersUsingProductId() {
        String productId = "332";


        APIResponse response = request.get("/getOrder", RequestOptions.create().setQueryParam("product_id", productId));

        Helper helper = new Helper(response);
        helper.logResponseDetails();

        final JSONObject responseObject = new JSONObject(response.text());
        final JSONArray ordersArray = responseObject.getJSONArray("orders");

        assertEquals(response.status(), 200);
        assertEquals(responseObject.get("message"), "Order found!!");
        assertEquals(ordersArray.getJSONObject(0).get("product_id"), productId);

    }

    @Test
    public void testTokenGeneration() {
        APIResponse response = request.post("/auth", RequestOptions.create().setData(getCredentials()));

        Helper helper = new Helper(response);
        helper.logResponseDetails();

        final JSONObject responseObject = new JSONObject(response.text());

        assertEquals(response.status(), 201);
        assertNotNull(responseObject.get("token"));
        assertEquals(responseObject.get("message"), "Authentication Successful!");
    }

    @Test
    public void testShouldUpdateTheOrderUsingPut() {

        APIResponse authResponse = request.post("/auth", RequestOptions.create().setData(getCredentials()));

        final JSONObject authResponseObject = new JSONObject(authResponse.text());
        String token = authResponseObject.get("token").toString();

        OrderData updatedOrder = getUpdatedOrder();

        int orderId = 2;
        APIResponse response = request.put("/updateOrder/" + orderId, RequestOptions.create()
                .setHeader("Authorization", token)
                .setData(updatedOrder));

        Helper helper = new Helper(response);
        helper.logResponseDetails();

        final JSONObject updateOrderResponseObject = new JSONObject(response.text());
        final JSONObject orderObject = updateOrderResponseObject.getJSONObject("order");

        assertEquals(response.status(), 200);
        assertEquals(updateOrderResponseObject.get("message"), "Order updated successfully!");
        assertEquals(orderId, orderObject.get("id"));
        assertEquals(updatedOrder.getUserId(), orderObject.get("user_id"));
        assertEquals(updatedOrder.getProductId(), orderObject.get("product_id"));
        assertEquals(updatedOrder.getProductName(), orderObject.get("product_name"));
        assertEquals(updatedOrder.getProductAmount(), orderObject.get("product_amount"));
        assertEquals(updatedOrder.getTotalAmt(), orderObject.get("total_amt"));
    }


}
