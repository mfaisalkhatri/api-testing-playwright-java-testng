package io.github.mfaisalkhatri.api.reqres.tests;

import java.util.HashMap;
import java.util.Map;

import io.github.mfaisalkhatri.api.manager.RequestManager;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

/**
 * @author Faisal Khatri
 * @since 2/28/2023
 **/
public class BaseTest {

    protected RequestManager manager;


    @BeforeClass
    public void setup() {
        String apiKey = System.getProperty ("api-key");
        this.manager = new RequestManager();
        this.manager.createPlaywright();
        final String baseUrl = "https://reqres.in";
        final Map<String, String> headers = new HashMap<>();
        headers.put("content-type", "application/json");
        headers.put ("x-api-key", apiKey);
        this.manager.setApiRequestContext(baseUrl, headers);
    }

    @AfterClass
    public void tearDown() {
        this.manager.disposeAPIRequestContext();
        this.manager.closePlaywright();
    }
}
