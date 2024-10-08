package io.github.mfaisalkhatri.api.restfulecommerce.testdata;

public class TokenBuilder {

    public static TokenData getCredentials() {
        return TokenData.builder ()
            .username ("admin")
            .password ("secretPass123")
            .build ();
    }
}
