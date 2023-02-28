package io.github.mfaisalkhatri.api.reqres.data;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Faisal Khatri
 * @since 2/28/2023
 **/
@Getter
@Builder
public class EmployeeData {

    private String name;
    private String job;
}
