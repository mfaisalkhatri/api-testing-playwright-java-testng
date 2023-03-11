package io.github.mfaisalkhatri.api.restfulbooker.tests;

import static io.github.mfaisalkhatri.api.restfulbooker.data.BookingDataBuilder.getBookingData;
import static io.github.mfaisalkhatri.api.restfulbooker.data.BookingDataBuilder.getPartialBookingData;
import static io.github.mfaisalkhatri.api.restfulbooker.data.TokenBuilder.getToken;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.RequestOptions;
import io.github.mfaisalkhatri.api.restfulbooker.data.BookingData;
import io.github.mfaisalkhatri.api.restfulbooker.data.PartialBookingData;
import io.github.mfaisalkhatri.api.restfulbooker.data.Tokencreds;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * @author Faisal Khatri
 * @since 2/28/2023
 **/
public class RestfulBookerEndToEndTests extends BaseTest {

    private int bookingId;
    private BookingData bookingData;
    private String token;

    @BeforeClass
    public void setupTest() {
        bookingData = getBookingData();
    }

    @Test
    public void createBookingTest() {
        APIResponse response = manager.postRequest("/booking", RequestOptions.create()
                .setData(bookingData));

        assertEquals(response.status(), 200);

        JSONObject responseObject = new JSONObject(response.text());
        assertNotNull(responseObject.get("bookingid"));

        JSONObject bookingObject = responseObject.getJSONObject("booking");
        JSONObject bookingDatesObject = bookingObject.getJSONObject("bookingdates");
        assertEquals(bookingData.getFirstname(), bookingObject.get("firstname"));
        assertEquals(bookingData.getBookingdates()
                .getCheckin(), bookingDatesObject.get("checkin"));
        bookingId = responseObject.getInt("bookingid");
    }

    @Test
    public void getBookingTest() {
        APIResponse response = manager.getRequest("/booking/" + bookingId);
        assertEquals(response.status(), 200);

        JSONObject responseObject = new JSONObject(response.text());
        JSONObject bookingDatesObject = responseObject.getJSONObject("bookingdates");

        assertEquals(bookingData.getFirstname(), responseObject.get("firstname"));
        assertEquals(bookingData.getBookingdates()
                .getCheckin(), bookingDatesObject.get("checkin"));
    }

    @Test
    public void updateBookingTest() {
        BookingData updateBookingData = getBookingData();
        APIResponse response = manager.putRequest("/booking/" + bookingId, RequestOptions.create()
                .setData(updateBookingData)
                .setHeader("Cookie", "token=" + token));
        assertEquals(response.status(), 200);

        JSONObject responseObject = new JSONObject(response.text());
        JSONObject bookingDatesObject = responseObject.getJSONObject("bookingdates");

        assertEquals(updateBookingData.getFirstname(), responseObject.get("firstname"));
        assertEquals(updateBookingData.getBookingdates()
                .getCheckout(), bookingDatesObject.get("checkout"));
    }

    @Test
    public void generateTokenTest() {
        Tokencreds tokenData = getToken();
        APIResponse response = manager.postRequest("/auth", RequestOptions.create()
                .setData(tokenData));
        assertEquals(response.status(), 200);

        JSONObject responseObject = new JSONObject(response.text());
        String tokenValue = responseObject.getString("token");
        assertNotNull(tokenValue);
        token = tokenValue;

    }

    @Test
    public void updatePartialBookingTest() {
        PartialBookingData partialBookingData = getPartialBookingData();

        APIResponse response = manager.patchRequest("/booking/" + bookingId, RequestOptions.create()
                .setData(partialBookingData)
                .setHeader("Cookie", "token=" + token));

        assertEquals(response.status(), 200);
        JSONObject responseObject = new JSONObject(response.text());

        assertEquals(partialBookingData.getFirstname(), responseObject.get("firstname"));
        assertEquals(partialBookingData.getTotalprice(), responseObject.get("totalprice"));
    }

    @Test
    public void deleteBookingTest() {
        APIResponse response = manager.deleteRequest("/booking/" + bookingId, RequestOptions.create()
                .setHeader("Cookie", "token=" + token));

        assertEquals(response.status(), 201);
    }

    @Test
    public void testBookingDeleted() {
        APIResponse response = manager.getRequest("/booking/" + bookingId);
        assertEquals(response.status(), 404);
    }
}
