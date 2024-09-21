package io.github.mfaisalkhatri.api.restfulecommerce;

import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.RequestOptions;
import io.github.mfaisalkhatri.api.restfulecommerce.testdata.OrderData;
import org.json.JSONObject;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static io.github.mfaisalkhatri.api.restfulecommerce.testdata.OrderDataBuilder.getNewOrder;
import static io.github.mfaisalkhatri.api.restfulecommerce.testdata.OrderDataBuilder.getOrderDataWithMissingProductId;
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
    public void testShouldNotFetchOrderWhenNoOrderExists() {

        final APIResponse response = this.request.get("/getAllOrders");

        final Logger logger = new Logger(response);
        logger.logResponseDetails();


        final JSONObject responseObject = new JSONObject(response.text());
        assertEquals(response.status(), 404);
        assertEquals(responseObject.get("message"), "No order found!!");
    }

    @Test
    public void testShouldNotFetchOrderWhenNoOrderExistsForOrderId() {

        final int orderId = 90;
        final APIResponse response = this.request.get("/getOrder", RequestOptions.create().setQueryParam("id", orderId));

        final Logger logger = new Logger(response);
        logger.logResponseDetails();

        final JSONObject responseObject = new JSONObject(response.text());

        assertEquals(response.status(), 404);
        assertEquals(responseObject.get("message"), "No order found with the given parameters!");
    }

    @Test
    public void testShouldNotFetchOrderWhenNoOrderExistsForUserId() {
        final String userId = "20";

        final APIResponse response = this.request.get("/getOrder", RequestOptions.create().setQueryParam("user_id", userId));

        final Logger logger = new Logger(response);
        logger.logResponseDetails();

        final JSONObject responseObject = new JSONObject(response.text());

        assertEquals(response.status(), 404);
        assertEquals(responseObject.get("message"), "No order found with the given parameters!");

    }

    @Test
    public void testShouldNotFetchOrderWhenNoOrderExistsForProductId() {
        final String productId = "987";

        final APIResponse response = this.request.get("/getOrder", RequestOptions.create().setQueryParam("product_id", productId));

        final Logger logger = new Logger(response);
        logger.logResponseDetails();

        final JSONObject responseObject = new JSONObject(response.text());

        assertEquals(response.status(), 404);
        assertEquals(responseObject.get("message"), "No order found with the given parameters!");
    }

    @Test
    public void testShouldNotGenerateTokenForInvalidCredentials() {


    }

}
