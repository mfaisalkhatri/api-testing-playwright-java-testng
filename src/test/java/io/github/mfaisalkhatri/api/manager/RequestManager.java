package io.github.mfaisalkhatri.api.manager;

import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;

import java.util.Map;

/**
 * @author Faisal Khatri
 * @since 2/28/2023
 **/
public class RequestManager {

    private Playwright playwright;
    private APIRequestContext apiRequestContext;

    public void createPlaywright() {
        this.playwright = Playwright.create();

    }

    public void setApiRequestContext(final String baseUrl, final Map<String, String> headers) {
        this.apiRequestContext = this.playwright.request()
                .newContext(new APIRequest.NewContextOptions().setBaseURL(baseUrl)
                        .setExtraHTTPHeaders(headers));
    }

    public APIResponse getRequest(final String endpoint) {
        return this.apiRequestContext.get(endpoint);
    }

    public APIResponse getRequest(final String endpoint, final RequestOptions options) {
        return this.apiRequestContext.get(endpoint, options);
    }

    public APIResponse postRequest(final String endpoint) {
        return this.apiRequestContext.post(endpoint);
    }

    public APIResponse postRequest(final String endpoint, final RequestOptions options) {
        return this.apiRequestContext.post(endpoint, options);
    }

    public APIResponse putRequest(final String endpoint) {
        return this.apiRequestContext.put(endpoint);
    }

    public APIResponse putRequest(final String endpoint, final RequestOptions options) {
        return this.apiRequestContext.put(endpoint, options);
    }

    public APIResponse patchRequest(final String endpoint) {
        return this.apiRequestContext.patch(endpoint);
    }

    public APIResponse patchRequest(final String endpoint, final RequestOptions options) {
        return this.apiRequestContext.patch(endpoint, options);

    }

    public APIResponse deleteRequest(final String endpoint) {
        return this.apiRequestContext.delete(endpoint);
    }

    public APIResponse deleteRequest(final String endpoint, final RequestOptions options) {
        return this.apiRequestContext.delete(endpoint, options);
    }

    public void disposeAPIRequestContext() {
        this.apiRequestContext.dispose();
    }

    public void closePlaywright() {
        this.playwright.close();
    }

}
