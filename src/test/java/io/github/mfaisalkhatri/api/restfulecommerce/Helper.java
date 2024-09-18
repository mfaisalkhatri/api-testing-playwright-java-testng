package io.github.mfaisalkhatri.api.restfulecommerce;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Helper {

    private APIResponse response;
    private Logger log;

    public Helper(APIResponse response) {
        this.response = response;
        log = LogManager.getLogger(getClass());
    }

    public void logResponseDetails() {

        log.info("Response Headers: \n{}",response.headers());
        log.info("Status Code: {}", response.status());
        log.info("Response Body: \n{}", prettyPrintJson(response.text()));
    }

    private String prettyPrintJson(String text) {
        String prettyPrintJson = "";
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Object jsonObject = objectMapper.readValue(response.text(), Object.class);
            prettyPrintJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);


        } catch (JsonProcessingException e) {
            log.error("Error Printing Pretty Json : {}", e.getMessage());
        }
        ;
        return prettyPrintJson;
    }
}
