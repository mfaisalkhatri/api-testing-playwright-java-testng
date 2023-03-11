package io.github.mfaisalkhatri.api.manager;

import java.util.Map;

import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;

/**
 * @author Faisal Khatri
 * @since 2/28/2023
 **/
public class RequestManager {

    private Playwright playwright;
    private APIRequestContext apiRequestContext;

    public void createPlaywright() {
        playwright = Playwright.create();

    }

    public void setApiRequestContext(String baseUrl, Map<String, String> headers) {
        apiRequestContext = playwright.request()
                .newContext(new APIRequest.NewContextOptions().setBaseURL(baseUrl)
                        .setExtraHTTPHeaders(headers));
    }

    public APIResponse getRequest(String endpoint) {
        return apiRequestContext.get(endpoint);
    }

    public APIResponse getRequest(String endpoint, RequestOptions options) {
        return apiRequestContext.get(endpoint, options);
    }

    public APIResponse postRequest(String endpoint) {
        return apiRequestContext.post(endpoint);
    }

    public APIResponse postRequest(String endpoint, RequestOptions options) {
        return apiRequestContext.post(endpoint, options);
    }

    public APIResponse putRequest(String endpoint) {
        return apiRequestContext.put(endpoint);
    }

    public APIResponse putRequest(String endpoint, RequestOptions options) {
        return apiRequestContext.put(endpoint, options);
    }

    public APIResponse patchRequest(String endpoint) {
        return apiRequestContext.patch(endpoint);
    }

    public APIResponse patchRequest(String endpoint, RequestOptions options) {
        return apiRequestContext.patch(endpoint, options);

    }

    public APIResponse deleteRequest(String endpoint) {
        return apiRequestContext.delete(endpoint);
    }

    public APIResponse deleteRequest(String endpoint, RequestOptions options) {
        return apiRequestContext.delete(endpoint, options);
    }

    public void disposeAPIRequestContext() {
        apiRequestContext.dispose();
    }

    public void closePlaywright() {
        playwright.close();
    }

}
