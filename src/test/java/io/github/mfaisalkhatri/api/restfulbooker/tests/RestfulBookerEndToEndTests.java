package io.github.mfaisalkhatri.api.restfulbooker.tests;

import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.RequestOptions;
import io.github.mfaisalkhatri.api.restfulbooker.data.BookingData;
import io.github.mfaisalkhatri.api.restfulbooker.data.PartialBookingData;
import io.github.mfaisalkhatri.api.restfulbooker.data.Tokencreds;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.github.mfaisalkhatri.api.restfulbooker.data.BookingDataBuilder.getBookingData;
import static io.github.mfaisalkhatri.api.restfulbooker.data.BookingDataBuilder.getPartialBookingData;
import static io.github.mfaisalkhatri.api.restfulbooker.data.TokenBuilder.getToken;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

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
        this.bookingData = getBookingData();
    }

    @Test
    public void createBookingTest() {
        final APIResponse response = this.manager.postRequest("/booking", RequestOptions.create()
                .setData(this.bookingData));

        assertEquals(response.status(), 200);

        final JSONObject responseObject = new JSONObject(response.text());
        assertNotNull(responseObject.get("bookingid"));

        final JSONObject bookingObject = responseObject.getJSONObject("booking");
        final JSONObject bookingDatesObject = bookingObject.getJSONObject("bookingdates");
        assertEquals(this.bookingData.getFirstname(), bookingObject.get("firstname"));
        assertEquals(this.bookingData.getBookingdates()
                .getCheckin(), bookingDatesObject.get("checkin"));
        this.bookingId = responseObject.getInt("bookingid");
    }

    @Test
    public void getBookingTest() {
        final APIResponse response = this.manager.getRequest("/booking/" + this.bookingId);
        assertEquals(response.status(), 200);

        final JSONObject responseObject = new JSONObject(response.text());
        final JSONObject bookingDatesObject = responseObject.getJSONObject("bookingdates");

        assertEquals(this.bookingData.getFirstname(), responseObject.get("firstname"));
        assertEquals(this.bookingData.getBookingdates()
                .getCheckin(), bookingDatesObject.get("checkin"));
    }

    @Test
    public void updateBookingTest() {
        final BookingData updateBookingData = getBookingData();
        final APIResponse response = this.manager.putRequest("/booking/" + this.bookingId, RequestOptions.create()
                .setData(updateBookingData)
                .setHeader("Cookie", "token=" + this.token));
        assertEquals(response.status(), 200);

        final JSONObject responseObject = new JSONObject(response.text());
        final JSONObject bookingDatesObject = responseObject.getJSONObject("bookingdates");

        assertEquals(updateBookingData.getFirstname(), responseObject.get("firstname"));
        assertEquals(updateBookingData.getBookingdates()
                .getCheckout(), bookingDatesObject.get("checkout"));
    }

    @Test
    public void generateTokenTest() {
        final Tokencreds tokenData = getToken();
        final APIResponse response = this.manager.postRequest("/auth", RequestOptions.create()
                .setData(tokenData));
        assertEquals(response.status(), 200);

        final JSONObject responseObject = new JSONObject(response.text());
        final String tokenValue = responseObject.getString("token");
        assertNotNull(tokenValue);
        this.token = tokenValue;

    }

    @Test
    public void updatePartialBookingTest() {
        final PartialBookingData partialBookingData = getPartialBookingData();

        final APIResponse response = this.manager.patchRequest("/booking/" + this.bookingId, RequestOptions.create()
                .setData(partialBookingData)
                .setHeader("Cookie", "token=" + this.token));

        assertEquals(response.status(), 200);
        final JSONObject responseObject = new JSONObject(response.text());

        assertEquals(partialBookingData.getFirstname(), responseObject.get("firstname"));
        assertEquals(partialBookingData.getTotalprice(), responseObject.get("totalprice"));
    }

    @Test
    public void deleteBookingTest() {
        final APIResponse response = this.manager.deleteRequest("/booking/" + this.bookingId, RequestOptions.create()
                .setHeader("Cookie", "token=" + this.token));

        assertEquals(response.status(), 201);
    }

    @Test
    public void testBookingDeleted() {
        final APIResponse response = this.manager.getRequest("/booking/" + this.bookingId);
        assertEquals(response.status(), 404);
    }
}
