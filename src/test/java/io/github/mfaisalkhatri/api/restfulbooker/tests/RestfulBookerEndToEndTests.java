package io.github.mfaisalkhatri.api.restfulbooker.tests;

import static io.github.mfaisalkhatri.api.restfulbooker.data.BookingDataBuilder.getBookingData;
import static io.github.mfaisalkhatri.api.restfulbooker.data.TokenBuilder.getToken;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.RequestOptions;
import io.github.mfaisalkhatri.api.restfulbooker.data.BookingData;
import io.github.mfaisalkhatri.api.restfulbooker.data.Tokencreds;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * @author Faisal Khatri
 * @since 2/28/2023
 **/
public class RestfulBookerEndToEndTests extends BaseTest {

    private int         bookingId;
    private BookingData bookingData;
    private String      token;

    @BeforeClass
    public void setupTest () {
        bookingData = getBookingData ();
    }

    @Test
    public void createBookingTest () {
        APIResponse response = manager.postRequest ("/booking", RequestOptions.create ()
            .setData (bookingData));

        JSONObject responseObject = new JSONObject (response.text ());
        JSONObject bookingObject = responseObject.getJSONObject ("booking");
        JSONObject bookingDatesObject = bookingObject.getJSONObject ("bookingdates");
        bookingId = responseObject.getInt ("bookingid");

        assertEquals (response.status (), 200);

        assertEquals (bookingData.getFirstname (), bookingObject.get ("firstname"));
        assertEquals (bookingData.getLastname (), bookingObject.get ("lastname"));
        assertEquals (bookingData.getTotalprice (), bookingObject.get ("totalprice"));
        assertEquals (bookingData.isDepositpaid (), bookingObject.get ("depositpaid"));
        assertEquals (bookingData.getBookingdates ()
            .getCheckin (), bookingDatesObject.get ("checkin"));
        assertEquals (bookingData.getBookingdates ()
            .getCheckout (), bookingDatesObject.get ("checkout"));
        assertEquals (bookingData.getAdditionalneeds (), bookingObject.get ("additionalneeds"));
    }

    @Test
    public void getBookingTest () {
        APIResponse response = manager.getRequest ("/booking/" + bookingId);
        assertEquals (response.status (), 200);

        JSONObject responseObject = new JSONObject (response.text ());
        JSONObject bookingDatesObject = responseObject.getJSONObject ("bookingdates");

        assertEquals (bookingData.getFirstname (), responseObject.get ("firstname"));
        assertEquals (bookingData.getLastname (), responseObject.get ("lastname"));
        assertEquals (bookingData.getTotalprice (), responseObject.get ("totalprice"));
        assertEquals (bookingData.isDepositpaid (), responseObject.get ("depositpaid"));
        assertEquals (bookingData.getBookingdates ()
            .getCheckin (), bookingDatesObject.get ("checkin"));
        assertEquals (bookingData.getBookingdates ()
            .getCheckout (), bookingDatesObject.get ("checkout"));
        assertEquals (bookingData.getAdditionalneeds (), responseObject.get ("additionalneeds"));
    }

    @Test
    public void updateBookingTest () {
        BookingData updateBookingData = getBookingData ();
        APIResponse response = manager.putRequest ("/booking/" + bookingId, RequestOptions.create ()
            .setData (updateBookingData)
            .setHeader ("Cookie", "token=" + token));
        assertEquals (response.status (), 200);

        JSONObject responseObject = new JSONObject (response.text ());
        JSONObject bookingDatesObject = responseObject.getJSONObject ("bookingdates");

        assertEquals (updateBookingData.getFirstname (), responseObject.get ("firstname"));
        assertEquals (updateBookingData.getLastname (), responseObject.get ("lastname"));
        assertEquals (updateBookingData.getTotalprice (), responseObject.get ("totalprice"));
        assertEquals (updateBookingData.isDepositpaid (), responseObject.get ("depositpaid"));
        assertEquals (updateBookingData.getBookingdates ()
            .getCheckin (), bookingDatesObject.get ("checkin"));
        assertEquals (updateBookingData.getBookingdates ()
            .getCheckout (), bookingDatesObject.get ("checkout"));
        assertEquals (updateBookingData.getAdditionalneeds (), responseObject.get ("additionalneeds"));


    }

    @Test
    public void generateTokenTest () {
        Tokencreds tokenData = getToken ();
        APIResponse response = manager.postRequest ("/auth", RequestOptions.create ()
            .setData (tokenData));
        assertEquals (response.status (), 200);
        JSONObject responseObject = new JSONObject (response.text ());
        String tokenValue = responseObject.getString ("token");
        assertNotNull (tokenValue);
        token = tokenValue;

    }
}
