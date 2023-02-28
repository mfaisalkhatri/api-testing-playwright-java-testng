package io.github.mfaisalkhatri.api.restfulbooker.data;

/**
 * @author Faisal Khatri
 * @since 2/28/2023
 **/
public class TokenBuilder {

    public static Tokencreds getToken () {
        return Tokencreds.builder ()
            .username ("admin")
            .password ("password123")
            .build ();
    }
}