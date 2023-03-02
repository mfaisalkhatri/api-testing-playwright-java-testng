package io.github.mfaisalkhatri.api.restfulbooker.tests;

import java.util.HashMap;
import java.util.Map;

import io.github.mfaisalkhatri.api.manager.RequestManager;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

/**
 * @author Faisal Khatri
 * @since 2/28/2023
 **/
public class BaseTest {

    protected RequestManager manager;

    @BeforeTest
    public void setupBase () {
        manager = new RequestManager ();
        manager.createPlaywright ();
        final String baseUrl = "http://localhost:3001";
        Map<String, String> headers = new HashMap<> ();
        headers.put ("content-type", "application/json");
        headers.put ("Accept", "application/json");
        manager.setApiRequestContext (baseUrl, headers);
    }

    @AfterTest
    public void tearDown () {
        manager.disposeAPIRequestContext ();
        manager.closePlaywright ();
    }
}
