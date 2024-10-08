package io.github.mfaisalkhatri.api.restfulecommerce;

import static io.github.mfaisalkhatri.api.restfulecommerce.testdata.OrderDataBuilder.getNewOrder;
import static io.github.mfaisalkhatri.api.restfulecommerce.testdata.OrderDataBuilder.getOrderDataWithMissingProductId;
import static io.github.mfaisalkhatri.api.restfulecommerce.testdata.OrderDataBuilder.getPartialUpdatedOrder;
import static io.github.mfaisalkhatri.api.restfulecommerce.testdata.OrderDataBuilder.getUpdatedOrder;
import static io.github.mfaisalkhatri.api.restfulecommerce.testdata.TokenBuilder.getCredentials;
import static io.github.mfaisalkhatri.api.restfulecommerce.testdata.TokenBuilder.getInvalidCredentials;
import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.RequestOptions;
import io.github.mfaisalkhatri.api.restfulecommerce.testdata.OrderData;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.testng.annotations.Test;

@Slf4j
public class SadPathTests extends BaseTest {


    @Test
    public void testShouldNotCreateOrder_WhenProductIdFieldIsMissing() {

        List<OrderData> orderList = new ArrayList<>();
        orderList.add(getOrderDataWithMissingProductId());

        final APIResponse response = this.request.post("/addOrder",
                RequestOptions.create().setData(orderList));

        logResponse (response);

        final JSONObject responseObject = new JSONObject(response.text());
        assertEquals(response.status(), 400);
        assertEquals (responseObject.get ("message"),
            "Each order must have user_id, product_id, product_name, product_amount, qty, tax_amt, and total_amt!");

    }

    @Test
    public void testShouldNotCreateOrder_WhenOrderListIsNotArray() {

        final APIResponse response = this.request.post("/addOrder",
                RequestOptions.create().setData(getNewOrder()));

        logResponse (response);

        final JSONObject responseObject = new JSONObject(response.text());

        assertEquals(response.status(), 400);
        assertEquals (responseObject.get ("message"), "Request Payload must be an array of orders!");
    }

    @Test
    public void testShouldNotFetchOrder_WhenNoOrderExists() {

        final APIResponse response = this.request.get("/getAllOrders");

        logResponse (response);

        final JSONObject responseObject = new JSONObject(response.text());
        assertEquals(response.status(), 404);
        assertEquals(responseObject.get("message"), "No Order found!!");
    }

    @Test
    public void testShouldNotFetchOrder_WhenNoOrderExistsForOrderId() {

        final int orderId = 90;
        final APIResponse response = this.request.get ("/getOrder", RequestOptions.create ()
            .setQueryParam ("id", orderId));

        logResponse (response);

        final JSONObject responseObject = new JSONObject(response.text());

        assertEquals(response.status(), 404);
        assertEquals(responseObject.get("message"), "No Order found with the given parameters!");
    }

    @Test
    public void testShouldNotFetchOrder_WhenNoOrderExistsForUserId() {
        final String userId = "20";

        final APIResponse response = this.request.get ("/getOrder", RequestOptions.create ()
            .setQueryParam ("user_id", userId));

        logResponse (response);

        final JSONObject responseObject = new JSONObject(response.text());

        assertEquals(response.status(), 404);
        assertEquals(responseObject.get("message"), "No Order found with the given parameters!");

    }

    @Test
    public void testShouldNotFetchOrder_WhenNoOrderExistsForProductId() {
        final String productId = "987";

        final APIResponse response = this.request.get ("/getOrder", RequestOptions.create ()
            .setQueryParam ("product_id", productId));

        logResponse (response);

        final JSONObject responseObject = new JSONObject(response.text());

        assertEquals(response.status(), 404);
        assertEquals(responseObject.get("message"), "No Order found with the given parameters!");
    }

    @Test
    public void testShouldNotUpdateOrder_WhenTokenIsMissing() {

        int orderId = 1;

        final OrderData updatedOrder = getUpdatedOrder();

        final APIResponse response = this.request.put ("/updateOrder/" + orderId, RequestOptions.create ()
                .setData(updatedOrder));

        logResponse (response);

        final JSONObject responseObject = new JSONObject(response.text());

        assertEquals(response.status(), 403);
        assertEquals(responseObject.get("message"), "Forbidden! Token is missing!");
    }

    @Test
    public void testShouldNotUpdateOrder_WhenOrderIdIsNotFound() {
        final APIResponse authResponse = this.request.post ("/auth", RequestOptions.create ()
            .setData (getCredentials ()));

        logResponse (authResponse);

        final JSONObject authResponseObject = new JSONObject(authResponse.text());
        final String token = authResponseObject.get("token").toString();

        final OrderData updatedOrder = getUpdatedOrder();

        final int orderId = 90;

        final APIResponse response = this.request.put("/updateOrder/" + orderId, RequestOptions.create()
                .setHeader("Authorization", token)
                .setData(updatedOrder));

        logResponse (response);

        final JSONObject responseObject = new JSONObject(response.text());

        assertEquals(response.status(), 404);
        assertEquals(responseObject.get("message"), "No Order found with the given Order Id!");

    }

    @Test
    public void testShouldNotUpdateOrder_WhenOrderDetailsAreNotProvided() {
        final APIResponse authResponse = this.request.post ("/auth", RequestOptions.create ()
            .setData (getCredentials ()));

        logResponse (authResponse);

        final JSONObject authResponseObject = new JSONObject(authResponse.text());
        final String token = authResponseObject.get("token").toString();

        final int orderId = 2;

        final APIResponse response = this.request.put("/updateOrder/" + orderId, RequestOptions.create()
                .setHeader("Authorization", token));

        logResponse (response);

        final JSONObject responseObject = new JSONObject(response.text());

        assertEquals(response.status(), 400);
        assertEquals(responseObject.get("message"), "Each Order must have user_id, product_id, product_name, product_amount, qty, tax_amt, and total_amt!");
    }

    @Test
    public void testShouldNotUpdateOrderWithInvalidToken() {
        final int orderId = 2;

        final APIResponse response = this.request.put("/updateOrder/" + orderId, RequestOptions.create()
                .setHeader("Authorization", "token273678"));

        logResponse (response);

        final JSONObject responseObject = new JSONObject(response.text());

        assertEquals(response.status(), 400);
        assertEquals(responseObject.get("message"), "Failed to authenticate token!");
    }

    @Test
    public void testShouldNotPartialUpdateOrder_WhenTokenIsMissing() {

        int orderId = 1;

        final OrderData partialUpdatedOrder = getPartialUpdatedOrder();

        final APIResponse response = this.request.patch ("/partialUpdateOrder/" + orderId, RequestOptions.create ()
                .setData(partialUpdatedOrder));

        logResponse (response);

        final JSONObject responseObject = new JSONObject(response.text());

        assertEquals(response.status(), 403);
        assertEquals(responseObject.get("message"), "Forbidden! Token is missing!");
    }

    @Test
    public void testShouldNotPartialUpdateOrder_WhenOrderIdIsNotFound() {
        final APIResponse authResponse = this.request.post ("/auth", RequestOptions.create ()
            .setData (getCredentials ()));

        logResponse (authResponse);

        final JSONObject authResponseObject = new JSONObject(authResponse.text());
        final String token = authResponseObject.get("token").toString();

        final OrderData updatedOrder = getPartialUpdatedOrder();

        final int orderId = 90;

        final APIResponse response = this.request.patch ("/partialUpdateOrder/" + orderId, RequestOptions.create ()
                .setHeader("Authorization", token)
                .setData(updatedOrder));

        logResponse (response);

        final JSONObject responseObject = new JSONObject(response.text());

        assertEquals(response.status(), 404);
        assertEquals(responseObject.get("message"), "No Order found with the given Order Id!");

    }

    @Test
    public void testShouldNotPartialUpdateOrder_WhenOrderDetailsAreNotProvided() {
        final APIResponse authResponse = this.request.post ("/auth", RequestOptions.create ()
            .setData (getCredentials ()));

        logResponse (authResponse);

        final JSONObject authResponseObject = new JSONObject(authResponse.text());
        final String token = authResponseObject.get("token").toString();

        final int orderId = 2;

        final APIResponse response = this.request.patch("/partialUpdateOrder/" + orderId, RequestOptions.create()
                .setHeader("Authorization", token));

        logResponse (response);

        final JSONObject responseObject = new JSONObject(response.text());

        assertEquals(response.status(), 400);
        assertEquals(responseObject.get("message"), "Invalid request, no data provided to update!");
    }

    @Test
    public void testShouldNotPartialUpdateOrderWithInvalidToken() {
        final int orderId = 2;

        final APIResponse response = this.request.patch ("/partialUpdateOrder/" + orderId, RequestOptions.create ()
                .setHeader("Authorization", "token273678"));

        logResponse (response);

        final JSONObject responseObject = new JSONObject(response.text());

        assertEquals(response.status(), 400);
        assertEquals(responseObject.get("message"), "Failed to authenticate token!");
    }

    @Test
    public void testShouldNotDeleteOrder_WhenTokenIsMissing () {

        int orderId = 1;

        final APIResponse response = this.request.delete ("/deleteOrder/" + orderId);

        logResponse (response);

        final JSONObject responseObject = new JSONObject (response.text ());

        assertEquals (response.status (), 403);
        assertEquals (responseObject.get ("message"), "Forbidden! Token is missing!");
    }

    @Test
    public void testShouldNotDeleteOrder_WhenOrderIdIsNotFound () {

        final APIResponse authResponse = this.request.post ("/auth", RequestOptions.create ()
            .setData (getCredentials ()));

        logResponse (authResponse);

        final JSONObject authResponseObject = new JSONObject (authResponse.text ());
        final String token = authResponseObject.get ("token")
            .toString ();

        final int orderId = 90;

        final APIResponse response = this.request.delete ("/deleteOrder/" + orderId, RequestOptions.create ()
            .setHeader ("Authorization", token));

        logResponse (response);

        final JSONObject responseObject = new JSONObject (response.text ());

        assertEquals (response.status (), 404);
        assertEquals (responseObject.get ("message"), "No Order found with the given Order Id!");
    }

    @Test
    public void testShouldNotDeleteOrderWithInvalidToken () {
        final int orderId = 2;

        final APIResponse response = this.request.delete ("/deleteOrder/" + orderId, RequestOptions.create ()
            .setHeader ("Authorization", "token273678"));

        logResponse (response);

        final JSONObject responseObject = new JSONObject (response.text ());

        assertEquals (response.status (), 400);
        assertEquals (responseObject.get ("message"), "Failed to authenticate token!");
    }

    @Test
    public void testShouldNotGenerateToken_ForInvalidCredentials () {

        final APIResponse response = this.request.post ("/auth", RequestOptions.create ()
            .setData (getInvalidCredentials ()));

        logResponse (response);

        final JSONObject responseObject = new JSONObject (response.text ());

        assertEquals (response.status (), 401);
        assertEquals (responseObject.get ("message"), "Authentication Failed! Invalid username or password!");
    }

    @Test
    public void testShouldNotGenerateToken_WhenCredentialsAreMissing () {

        final APIResponse response = this.request.post ("/auth", RequestOptions.create ());

        logResponse (response);

        final JSONObject responseObject = new JSONObject (response.text ());

        assertEquals (response.status (), 400);
        assertEquals (responseObject.get ("message"), "Username and Password is required for authentication!");
    }

}
