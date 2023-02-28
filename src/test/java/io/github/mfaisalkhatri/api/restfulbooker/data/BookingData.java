package io.github.mfaisalkhatri.api.restfulbooker.data;

import lombok.Builder;
import lombok.Getter;

/**
 * @author Faisal Khatri
 * @since 2/28/2023
 **/
@Getter
@Builder
public class BookingData {
    private String       firstname;
    private String       lastname;
    private int          totalprice;
    private boolean depositpaid;
    private BookingDates bookingdates;
    private String       additionalneeds;

}
