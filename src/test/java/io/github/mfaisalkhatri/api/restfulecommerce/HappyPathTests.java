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

import static io.github.mfaisalkhatri.api.restfulecommerce.testdata.OrderDataBuilder.*;
import static io.github.mfaisalkhatri.api.restfulecommerce.testdata.TokenBuilder.getCredentials;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class HappyPathTests extends BaseTest{

    private List<OrderData> orderList;

    @BeforeClass
    public void testSetup() {
        this.orderList = new ArrayList<>();
    }

    @Test
    public void testShouldCreateNewOrders() throws JsonProcessingException {

        final int totalOrders = 4;

        for (int i = 0; i < totalOrders; i++) {
            this.orderList.add(getNewOrder());
        }

        final APIResponse response = this.request.post("/addOrder", RequestOptions.create()
                .setData(this.orderList));
        final Helper helper = new Helper(response);
        helper.logResponseDetails();

        final JSONObject responseObject = new JSONObject(response.text());
        final JSONArray ordersArray = responseObject.getJSONArray("orders");

        assertEquals(response.status(), 201);
        assertEquals(responseObject.get("message"), "Orders added successfully!");
        assertNotNull(ordersArray.getJSONObject(0).get("id"));
        assertEquals(this.orderList.get(0).getUserId(), ordersArray.getJSONObject(0).get("user_id"));
        assertEquals(this.orderList.get(0).getProductId(), ordersArray.getJSONObject(0).get("product_id"));
        assertEquals(this.orderList.get(0).getTotalAmt(), ordersArray.getJSONObject(0).get("total_amt"));
    }

    @Test
    public void testShouldGetAllOrders() {

        final APIResponse response = this.request.get("/getAllOrders");

        final Helper helper = new Helper(response);
        helper.logResponseDetails();

        final JSONObject responseObject = new JSONObject(response.text());
        final JSONArray ordersArray = responseObject.getJSONArray("orders");

        assertEquals(response.status(), 200);
        assertEquals(responseObject.get("message"), "Orders fetched successfully!");
        assertEquals(this.orderList.get(0).getUserId(), ordersArray.getJSONObject(0).get("user_id"));
        assertEquals(this.orderList.get(0).getProductId(), ordersArray.getJSONObject(0).get("product_id"));
        assertEquals(this.orderList.get(0).getTotalAmt(), ordersArray.getJSONObject(0).get("total_amt"));
    }

    @Test
    public void testShouldGetOrderUsingOrderId() {
        final int orderId = 1;
        final APIResponse response = this.request.get("/getOrder", RequestOptions.create().setQueryParam("id", orderId));

        final Helper helper = new Helper(response);
        helper.logResponseDetails();

        final JSONObject responseObject = new JSONObject(response.text());
        final JSONArray ordersArray = responseObject.getJSONArray("orders");

        assertEquals(response.status(), 200);
        assertEquals(ordersArray.getJSONObject(0).get("id"), orderId);
        assertEquals(responseObject.get("message"), "Order found!!");

    }

    @Test
    public void testShouldGetOrdersUsingUserId() {
        final String userId = "2";

        final APIResponse response = this.request.get("/getOrder", RequestOptions.create().setQueryParam("user_id", userId));

        final Helper helper = new Helper(response);
        helper.logResponseDetails();

        final JSONObject responseObject = new JSONObject(response.text());
        final JSONArray ordersArray = responseObject.getJSONArray("orders");

        assertEquals(response.status(), 200);
        assertEquals(responseObject.get("message"), "Order found!!");
        assertEquals(ordersArray.getJSONObject(0).get("user_id"), userId);

    }

    @Test
    public void testShouldGetOrdersUsingProductId() {
        final String productId = "332";


        final APIResponse response = this.request.get("/getOrder", RequestOptions.create().setQueryParam("product_id", productId));

        final Helper helper = new Helper(response);
        helper.logResponseDetails();

        final JSONObject responseObject = new JSONObject(response.text());
        final JSONArray ordersArray = responseObject.getJSONArray("orders");

        assertEquals(response.status(), 200);
        assertEquals(responseObject.get("message"), "Order found!!");
        assertEquals(ordersArray.getJSONObject(0).get("product_id"), productId);

    }

    @Test
    public void testTokenGeneration() {
        final APIResponse response = this.request.post("/auth", RequestOptions.create().setData(getCredentials()));

        final Helper helper = new Helper(response);
        helper.logResponseDetails();

        final JSONObject responseObject = new JSONObject(response.text());

        assertEquals(response.status(), 201);
        assertNotNull(responseObject.get("token"));
        assertEquals(responseObject.get("message"), "Authentication Successful!");
    }

    @Test
    public void testShouldUpdateTheOrderUsingPut() {

        final APIResponse authResponse = this.request.post("/auth", RequestOptions.create().setData(getCredentials()));

        final JSONObject authResponseObject = new JSONObject(authResponse.text());
        final String token = authResponseObject.get("token").toString();

        final OrderData updatedOrder = getUpdatedOrder();

        final int orderId = 2;
        final APIResponse response = this.request.put("/updateOrder/" + orderId, RequestOptions.create()
                .setHeader("Authorization", token)
                .setData(updatedOrder));

        final Helper helper = new Helper(response);
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

    @Test
    public void testShouldUpdateTheOrderUsingPatch() {

        final APIResponse authResponse = this.request.post("/auth", RequestOptions.create().setData(getCredentials()));

        final JSONObject authResponseObject = new JSONObject(authResponse.text());
        final String token = authResponseObject.get("token").toString();

        final OrderData partialUpdatedOrder = getPartialUpdatedOrder();

        final int orderId = 1;
        final APIResponse response = this.request.patch("/partialUpdateOrder/" + orderId, RequestOptions.create()
                .setHeader("Authorization", token)
                .setData(partialUpdatedOrder));

        final Helper helper = new Helper(response);
        helper.logResponseDetails();

        final JSONObject updateOrderResponseObject = new JSONObject(response.text());
        final JSONObject orderObject = updateOrderResponseObject.getJSONObject("order");

        assertEquals(response.status(), 200);
        assertEquals(updateOrderResponseObject.get("message"), "Order updated successfully!");
        assertEquals(orderId, orderObject.get("id"));
        assertEquals(partialUpdatedOrder.getProductAmount(), orderObject.get("product_amount"));
        assertEquals(partialUpdatedOrder.getQty(), orderObject.get("qty"));
    }

    @Test
    public void testShouldDeleteTheOrder() {

        final APIResponse authResponse = this.request.post("/auth", RequestOptions.create().setData(getCredentials()));

        final JSONObject authResponseObject = new JSONObject(authResponse.text());
        final String token = authResponseObject.get("token").toString();


        final int orderId = 1;
        final APIResponse response = this.request.delete("/deleteOrder/" + orderId, RequestOptions.create()
                .setHeader("Authorization", token));

        final Helper helper = new Helper(response);
        helper.logResponseDetails();

        assertEquals(response.status(), 204);
    }

    @Test
    public void testShouldNotRetrieveDeletedOrder() {
        final int orderId = 1;
        final APIResponse response = this.request.get("/getOrder", RequestOptions.create().setQueryParam("id", orderId));

        final Helper helper = new Helper(response);
        helper.logResponseDetails();

        assertEquals(response.status(), 404);

        JSONObject jsonObject = new JSONObject(response.text());
        assertEquals(jsonObject.get("message"), "No Order found with the given parameters!");
    }
    
}
