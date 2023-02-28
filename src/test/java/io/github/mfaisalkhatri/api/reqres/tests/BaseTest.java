package io.github.mfaisalkhatri.api.reqres.tests;

import java.util.HashMap;
import java.util.Map;

import io.github.mfaisalkhatri.api.reqres.requests.RequestManager;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

/**
 * @author Faisal Khatri
 * @since 2/28/2023
 **/
public class BaseTest {

    protected RequestManager manager;
    @BeforeClass
    public void setup () {
        manager = new RequestManager ();
        manager.createPlaywright ();
        String baseUrl = "https://reqres.in";
        Map<String, String> headers = new HashMap<> ();
        headers.put ("content-type", "application/json");
        manager.setApiRequestContext (baseUrl,headers);
    }

    @AfterClass
    public void tearDown() {
        manager.disposeAPIRequestContext ();
        manager.closePlaywright ();
    }
}
