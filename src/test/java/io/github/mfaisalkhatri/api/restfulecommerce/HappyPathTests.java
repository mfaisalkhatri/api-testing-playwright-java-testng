package io.github.mfaisalkhatri.api.restfulecommerce;

import static io.github.mfaisalkhatri.api.restfulecommerce.testdata.OrderDataBuilder.getNewOrder;
import static io.github.mfaisalkhatri.api.restfulecommerce.testdata.OrderDataBuilder.getPartialUpdatedOrder;
import static io.github.mfaisalkhatri.api.restfulecommerce.testdata.OrderDataBuilder.getUpdatedOrder;
import static io.github.mfaisalkhatri.api.restfulecommerce.testdata.TokenBuilder.getCredentials;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.RequestOptions;
import io.github.mfaisalkhatri.api.restfulecommerce.testdata.OrderData;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Slf4j
public class HappyPathTests extends BaseTest{

    private List<OrderData> orderList;
    private int orderId;
    private String userId;
    private String productId;

    @BeforeClass
    public void testSetup() {
        this.orderList = new ArrayList<>();
    }

    @Test
    public void testShouldPerformHealthCheckOfServer() {
        final APIResponse response = this.request.get("/health");

        logResponse (response);
        final JSONObject responseObject = new JSONObject(response.text());

        assertEquals(response.status(), 200);
        assertEquals(responseObject.get("status"), "UP and Running");
    }

    @Test
    public void testShouldCreateNewOrders() {

        final int totalOrders = 4;

        for (int i = 0; i < totalOrders; i++) {
            this.orderList.add(getNewOrder());
        }

        final APIResponse response = this.request.post("/addOrder", RequestOptions.create()
                .setData(this.orderList));

        logResponse (response);

        final JSONObject responseObject = new JSONObject(response.text());
        final JSONArray ordersArray = responseObject.getJSONArray("orders");

        assertEquals(response.status(), 201);
        assertEquals(responseObject.get("message"), "Orders added successfully!");
        assertNotNull(ordersArray.getJSONObject(0).get("id"));
        assertEquals(this.orderList.get(0).getUserId(), ordersArray.getJSONObject(0).get("user_id"));
        assertEquals(this.orderList.get(0).getProductId(), ordersArray.getJSONObject(0).get("product_id"));
        assertEquals(this.orderList.get(0).getTotalAmt(), ordersArray.getJSONObject(0).get("total_amt"));

        this.orderId = ordersArray.getJSONObject(0).getInt("id");
        this.userId =ordersArray.getJSONObject(0).getString("user_id");
        this.productId =ordersArray.getJSONObject(0).getString("product_id");

    }

    @Test
    public void testShouldGetAllOrders() {

        final APIResponse response = this.request.get("/getAllOrders");

        logResponse (response);

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
        final APIResponse response = this.request.get ("/getOrder", RequestOptions.create ()
            .setQueryParam ("id", orderId));

        logResponse (response);

        final JSONObject responseObject = new JSONObject(response.text());
        final JSONArray ordersArray = responseObject.getJSONArray("orders");

        assertEquals(response.status(), 200);
        assertEquals(ordersArray.getJSONObject(0).get("id"), orderId);
        assertEquals(responseObject.get("message"), "Order found!!");

    }

    @Test
    public void testShouldGetOrdersUsingUserId() {

        final APIResponse response = this.request.get ("/getOrder", RequestOptions.create ()
            .setQueryParam ("user_id", this.userId));

        logResponse (response);

        final JSONObject responseObject = new JSONObject(response.text());
        final JSONArray ordersArray = responseObject.getJSONArray("orders");

        assertEquals(response.status(), 200);
        assertEquals(responseObject.get("message"), "Order found!!");
        assertEquals(ordersArray.getJSONObject(0).get("user_id"), this.userId);

    }

    @Test
    public void testShouldGetOrdersUsingProductId() {

        final APIResponse response = this.request.get ("/getOrder", RequestOptions.create ()
            .setQueryParam ("product_id", this.productId));

        logResponse (response);

        final JSONObject responseObject = new JSONObject(response.text());
        final JSONArray ordersArray = responseObject.getJSONArray("orders");

        assertEquals(response.status(), 200);
        assertEquals(responseObject.get("message"), "Order found!!");
        assertEquals(ordersArray.getJSONObject(0).get("product_id"), this.productId);

    }

    @Test
    public void testShouldGetOrdersUsingOrderIdProductIdAndUserId() {

        final APIResponse response = this.request.get("/getOrder", RequestOptions.create()
                .setQueryParam("id", this.orderId)
                .setQueryParam("product_id", this.productId)
                .setQueryParam("user_id", this.userId));

        logResponse (response);

        assertEquals(response.status(), 200);

        final JSONObject responseObject = new JSONObject(response.text());
        final JSONArray ordersArray = responseObject.getJSONArray("orders");

        assertEquals(responseObject.get("message"), "Order found!!");
        assertEquals(ordersArray.getJSONObject(0).get("id"), this.orderId);
        assertEquals(ordersArray.getJSONObject(0).get("product_id"), this.productId);
        assertEquals(ordersArray.getJSONObject(0).get("user_id"), this.userId);

    }


    @Test
    public void testTokenGeneration() {
        final APIResponse response = this.request.post ("/auth", RequestOptions.create ()
            .setData (getCredentials ()));

        logResponse (response);

        final JSONObject responseObject = new JSONObject(response.text());

        assertEquals(response.status(), 201);
        assertNotNull(responseObject.get("token"));
        assertEquals(responseObject.get("message"), "Authentication Successful!");
    }

    @Test
    public void testShouldUpdateTheOrderUsingPut() {

        final APIResponse authResponse = this.request.post ("/auth", RequestOptions.create ()
            .setData (getCredentials ()));

        logResponse (authResponse);

        final JSONObject authResponseObject = new JSONObject(authResponse.text());
        final String token = authResponseObject.get("token").toString();

        final OrderData updatedOrder = getUpdatedOrder();

        final int orderId = 2;
        final APIResponse response = this.request.put("/updateOrder/" + orderId, RequestOptions.create()
                .setHeader("Authorization", token)
                .setData(updatedOrder));

        logResponse (response);

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
    public void testShouldPartialUpdateTheOrderUsingPatch() {

        final APIResponse authResponse = this.request.post("/auth", RequestOptions.create().setData(getCredentials()));

        logResponse (authResponse);

        final JSONObject authResponseObject = new JSONObject(authResponse.text());
        final String token = authResponseObject.get("token").toString();

        final OrderData partialUpdatedOrder = getPartialUpdatedOrder();

        final int orderId = 1;
        final APIResponse response = this.request.patch("/partialUpdateOrder/" + orderId, RequestOptions.create()
                .setHeader("Authorization", token)
                .setData(partialUpdatedOrder));

        logResponse (response);

        final JSONObject updateOrderResponseObject = new JSONObject(response.text());
        final JSONObject orderObject = updateOrderResponseObject.getJSONObject ("order");

        assertEquals(response.status(), 200);
        assertEquals(updateOrderResponseObject.get("message"), "Order updated successfully!");
        assertEquals(orderId, orderObject.get("id"));
        assertEquals(partialUpdatedOrder.getProductAmount(), orderObject.get("product_amount"));
        assertEquals(partialUpdatedOrder.getQty(), orderObject.get("qty"));
    }

    @Test
    public void testShouldDeleteTheOrder() {

        final APIResponse authResponse = this.request.post ("/auth", RequestOptions.create ()
            .setData (getCredentials ()));

        logResponse (authResponse);

        final JSONObject authResponseObject = new JSONObject(authResponse.text());
        final String token = authResponseObject.get("token").toString();


        final int orderId = 1;
        final APIResponse response = this.request.delete("/deleteOrder/" + orderId, RequestOptions.create()
                .setHeader("Authorization", token));

        logResponse (response);
        assertEquals(response.status(), 204);
    }

    @Test
    public void testShouldNotRetrieveDeletedOrder() {
        final int orderId = 1;
        final APIResponse response = this.request.get ("/getOrder", RequestOptions.create ()
            .setQueryParam ("id", orderId));

        logResponse (response);

        assertEquals(response.status(), 404);

        final JSONObject jsonObject = new JSONObject(response.text());
        assertEquals(jsonObject.get("message"), "No Order found with the given parameters!");
    }

}
