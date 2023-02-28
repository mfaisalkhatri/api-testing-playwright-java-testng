package io.github.mfaisalkhatri.api.restfulbooker.data;

import lombok.Builder;
import lombok.Getter;

/**
 * @author Faisal Khatri
 * @since 2/28/2023
 **/
@Getter
@Builder
public class BookingDates {
    private String checkin;
    private String checkout;
}
