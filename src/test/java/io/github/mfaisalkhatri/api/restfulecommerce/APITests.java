package io.github.mfaisalkhatri.api.restfulecommerce;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Request;
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


    @Test
    public void testShouldCreateNewOrders() throws JsonProcessingException {

        int totalOrders = 4;
        List<OrderData> orderList = new ArrayList<>();
        for (int i = 0; i < totalOrders; i++) {
            orderList.add(getNewOrder());
        }

        APIResponse response = request.post("/addOrder", RequestOptions.create()
                .setData(orderList));
        Helper helper = new Helper(response);

        helper.logResponseDetails();

        assertEquals(response.status(), 201);

        final JSONObject responseObject = new JSONObject(response.text());
        final JSONArray ordersArray = responseObject.getJSONArray("orders");


        assertEquals(responseObject.get("message"), "Orders added successfully!");
        assertNotNull(ordersArray.getJSONObject(0).get("id"));
        assertEquals(orderList.get(0).getUserId(), ordersArray.getJSONObject(0).get("user_id"));
        assertEquals(orderList.get(0).getUserId(), ordersArray.getJSONObject(0).get("product_id"));
    }

    @Test
    public void testShouldGetAllOrders() {

        





    }
}
