package io.github.mfaisalkhatri.api.restfulecommerce;

import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.RequestOptions;
import io.github.mfaisalkhatri.api.restfulecommerce.testdata.OrderData;
import org.json.JSONObject;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static io.github.mfaisalkhatri.api.restfulecommerce.testdata.OrderDataBuilder.*;
import static io.github.mfaisalkhatri.api.restfulecommerce.testdata.TokenBuilder.getCredentials;
import static org.testng.Assert.assertEquals;

public class SadPathTests extends BaseTest {


    @Test
    public void testShouldNotCreateOrder_WhenProductIdFieldIsMissing() {

        List<OrderData> orderList = new ArrayList<>();
        orderList.add(getOrderDataWithMissingProductId());

        final APIResponse response = this.request.post("/addOrder",
                RequestOptions.create().setData(orderList));

        final JSONObject responseObject = new JSONObject(response.text());
        assertEquals(response.status(), 400);
        assertEquals(responseObject.get("message"), "Each order must have user_id, product_id, product_name, product_amount, qty, tax_amt, and total_amt!");


    }

    @Test
    public void testShouldNotCreateOrder_WhenOrderListIsNotArray() {

        final APIResponse response = this.request.post("/addOrder",
                RequestOptions.create().setData(getNewOrder()));

        final JSONObject responseObject = new JSONObject(response.text());
        assertEquals(response.status(), 400);
        assertEquals(responseObject.get("message"), "Request Payload must be an array of orders!");
    }

    @Test
    public void testShouldNotFetchOrder_WhenNoOrderExists() {

        final APIResponse response = this.request.get("/getAllOrders");

        final Logger logger = new Logger(response);
        logger.logResponseDetails();


        final JSONObject responseObject = new JSONObject(response.text());
        assertEquals(response.status(), 404);
        assertEquals(responseObject.get("message"), "No Order found!!");
    }

    @Test
    public void testShouldNotFetchOrder_WhenNoOrderExistsForOrderId() {

        final int orderId = 90;
        final APIResponse response = this.request.get("/getOrder", RequestOptions.create().setQueryParam("id", orderId));

        final Logger logger = new Logger(response);
        logger.logResponseDetails();

        final JSONObject responseObject = new JSONObject(response.text());

        assertEquals(response.status(), 404);
        assertEquals(responseObject.get("message"), "No Order found with the given parameters!");
    }

    @Test
    public void testShouldNotFetchOrder_WhenNoOrderExistsForUserId() {
        final String userId = "20";

        final APIResponse response = this.request.get("/getOrder", RequestOptions.create().setQueryParam("user_id", userId));

        final Logger logger = new Logger(response);
        logger.logResponseDetails();

        final JSONObject responseObject = new JSONObject(response.text());

        assertEquals(response.status(), 404);
        assertEquals(responseObject.get("message"), "No Order found with the given parameters!");

    }

    @Test
    public void testShouldNotFetchOrder_WhenNoOrderExistsForProductId() {
        final String productId = "987";

        final APIResponse response = this.request.get("/getOrder", RequestOptions.create().setQueryParam("product_id", productId));

        final Logger logger = new Logger(response);
        logger.logResponseDetails();

        final JSONObject responseObject = new JSONObject(response.text());

        assertEquals(response.status(), 404);
        assertEquals(responseObject.get("message"), "No Order found with the given parameters!");
    }

    @Test
    public void testShouldNotUpdateOrder_WhenTokenIsMissing() {

        int orderId = 1;

        final OrderData updatedOrder = getUpdatedOrder();

        final APIResponse response = this.request.put("/updateOrder/" + orderId, RequestOptions.create()
                .setData(updatedOrder));

        final JSONObject responseObject = new JSONObject(response.text());

        assertEquals(response.status(), 403);
        assertEquals(responseObject.get("message"), "Forbidden! Token is missing!");
    }

    @Test
    public void testShouldNotUpdateOrder_WhenOrderIdIsNotFound() {
        final APIResponse authResponse = this.request.post("/auth", RequestOptions.create().setData(getCredentials()));

        final JSONObject authResponseObject = new JSONObject(authResponse.text());
        final String token = authResponseObject.get("token").toString();

        final OrderData updatedOrder = getUpdatedOrder();

        final int orderId = 90;

        final APIResponse response = this.request.put("/updateOrder/" + orderId, RequestOptions.create()
                .setHeader("Authorization", token)
                .setData(updatedOrder));


        final JSONObject responseObject = new JSONObject(response.text());

        assertEquals(response.status(), 404);
        assertEquals(responseObject.get("message"), "No Order found with the given Order Id!");

    }

    @Test
    public void testShouldNotUpdateOrder_WhenOrderDetailsAreNotProvided() {
        final APIResponse authResponse = this.request.post("/auth", RequestOptions.create().setData(getCredentials()));

        final JSONObject authResponseObject = new JSONObject(authResponse.text());
        final String token = authResponseObject.get("token").toString();

        final int orderId = 2;

        final APIResponse response = this.request.put("/updateOrder/" + orderId, RequestOptions.create()
                .setHeader("Authorization", token));

        final JSONObject responseObject = new JSONObject(response.text());

        assertEquals(response.status(), 400);
        assertEquals(responseObject.get("message"), "Each Order must have user_id, product_id, product_name, product_amount, qty, tax_amt, and total_amt!");
    }

    @Test
    public void testShouldNotUpdateOrderWithInvalidToken() {
        final int orderId = 2;

        final APIResponse response = this.request.put("/updateOrder/" + orderId, RequestOptions.create()
                .setHeader("Authorization", "token273678"));

        final JSONObject responseObject = new JSONObject(response.text());

        assertEquals(response.status(), 400);
        assertEquals(responseObject.get("message"), "Failed to authenticate token!");
    }

    @Test
    public void testShouldNotPartialUpdateOrder_WhenTokenIsMissing() {

        int orderId = 1;

        final OrderData partialUpdatedOrder = getPartialUpdatedOrder();

        final APIResponse response = this.request.patch("/partialUpdateOrder/" + orderId, RequestOptions.create()
                .setData(partialUpdatedOrder));

        final JSONObject responseObject = new JSONObject(response.text());

        assertEquals(response.status(), 403);
        assertEquals(responseObject.get("message"), "Forbidden! Token is missing!");
    }

    @Test
    public void testShouldNotPartialUpdateOrder_WhenOrderIdIsNotFound() {
        final APIResponse authResponse = this.request.post("/auth", RequestOptions.create().setData(getCredentials()));

        final JSONObject authResponseObject = new JSONObject(authResponse.text());
        final String token = authResponseObject.get("token").toString();

        final OrderData updatedOrder = getPartialUpdatedOrder();

        final int orderId = 90;

        final APIResponse response = this.request.patch("/partialUpdateOrder/" + orderId, RequestOptions.create()
                .setHeader("Authorization", token)
                .setData(updatedOrder));


        final JSONObject responseObject = new JSONObject(response.text());

        assertEquals(response.status(), 404);
        assertEquals(responseObject.get("message"), "No Order found with the given Order Id!");

    }

    @Test
    public void testShouldNotPartialUpdateOrder_WhenOrderDetailsAreNotProvided() {
        final APIResponse authResponse = this.request.post("/auth", RequestOptions.create().setData(getCredentials()));

        final JSONObject authResponseObject = new JSONObject(authResponse.text());
        final String token = authResponseObject.get("token").toString();

        final int orderId = 2;

        final APIResponse response = this.request.patch("/partialUpdateOrder/" + orderId, RequestOptions.create()
                .setHeader("Authorization", token));

        final JSONObject responseObject = new JSONObject(response.text());

        assertEquals(response.status(), 400);
        assertEquals(responseObject.get("message"), "Invalid request, no data provided to update!");
    }

    @Test
    public void testShouldNotPartialUpdateOrderWithInvalidToken() {
        final int orderId = 2;

        final APIResponse response = this.request.patch("/partialUpdateOrder/" + orderId, RequestOptions.create()
                .setHeader("Authorization", "token273678"));

        final JSONObject responseObject = new JSONObject(response.text());

        assertEquals(response.status(), 400);
        assertEquals(responseObject.get("message"), "Failed to authenticate token!");
    }
}
