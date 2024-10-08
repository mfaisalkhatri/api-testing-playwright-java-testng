package io.github.mfaisalkhatri.api.restfulecommerce;

import java.util.HashMap;
import java.util.Map;

import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

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
        this.playwright = Playwright.create ();
    }

    private void createAPIRequestContext() {
        final Map<String, String> headers = new HashMap<> ();
        headers.put("Content-Type", "application/json");

        this.request = this.playwright.request ()
            .newContext (new APIRequest.NewContextOptions ()
                .setBaseURL(BASE_URL)
                .setExtraHTTPHeaders(headers));
    }


    private void closePlaywright() {
        if (this.playwright != null) {
            this.playwright.close ();
            this.playwright = null;
        }
    }

    private void disposeAPIRequestContext() {
        if (this.request != null) {
            this.request.dispose ();
            this.request = null;
        }
    }

    protected void logResponse (final APIResponse response) {
        final Logger logger = new Logger (response);
        logger.logResponseDetails ();
    }
}
