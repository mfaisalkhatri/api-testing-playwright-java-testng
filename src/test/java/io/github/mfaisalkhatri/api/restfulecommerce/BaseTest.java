package io.github.mfaisalkhatri.api.restfulecommerce;

import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.Playwright;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.util.HashMap;
import java.util.Map;

public class BaseTest {

    protected Playwright playwright;
    protected APIRequestContext request;
    private static final String BASE_URL = "http://localhost:3004";

    @BeforeClass
    public void setup() {
        createPlaywright();
        createAPIRequestContext();
    }

    @AfterClass
    public void tearDown() {
        disposeAPIRequestContext();
        closePlaywright();

    }

    private void createPlaywright() {
        playwright = Playwright.create();
    }

    private void createAPIRequestContext() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        request = playwright.request().newContext(new APIRequest.NewContextOptions()
                .setBaseURL(BASE_URL)
                .setExtraHTTPHeaders(headers));
    }


    private void closePlaywright() {
        if (playwright != null) {
            playwright.close();
            playwright = null;
        }
    }

    private void disposeAPIRequestContext() {
        if (request != null) {
            request.dispose();
            request = null;
        }
    }


}
