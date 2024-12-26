package io.github.mfaisalkhatri.api.restfulbooker.tests;

import java.util.HashMap;
import java.util.Map;

import com.microsoft.playwright.APIResponse;
import io.github.mfaisalkhatri.api.manager.RequestManager;
import io.github.mfaisalkhatri.api.restfulecommerce.Logger;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

/**
 * @author Faisal Khatri
 * @since 2/28/2023
 **/
public class BaseTest {

    protected RequestManager manager;

    @BeforeTest
    public void setupBase() {
        this.manager = new RequestManager();
        this.manager.createPlaywright();
        final String baseUrl = "http://localhost:3001";
        final Map<String, String> headers = new HashMap<>();
        headers.put("content-type", "application/json");
        headers.put("Accept", "application/json");
        this.manager.setApiRequestContext(baseUrl, headers);
    }

    @AfterTest
    public void tearDown() {
        this.manager.disposeAPIRequestContext();
        this.manager.closePlaywright();
    }

    protected void logResponse (final APIResponse response) {
        final Logger logger = new Logger (response);
        logger.logResponseDetails ();
    }
}
