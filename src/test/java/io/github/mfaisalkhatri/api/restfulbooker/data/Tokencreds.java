package io.github.mfaisalkhatri.api.restfulbooker.data;

import lombok.Builder;
import lombok.Getter;

/**
 * @author Faisal Khatri
 * @since 2/28/2023
 **/
@Builder
@Getter
public class Tokencreds {

    private String username;
    private String password;

}