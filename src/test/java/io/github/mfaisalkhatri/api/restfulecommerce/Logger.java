package io.github.mfaisalkhatri.api.restfulecommerce;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIResponse;
import org.apache.logging.log4j.LogManager;


public class Logger {

    private APIResponse response;
    private org.apache.logging.log4j.Logger log;

    public Logger(APIResponse response) {
        this.response = response;
        log = LogManager.getLogger(getClass());
    }

    public void logResponseDetails() {

        log.info("Response Headers: \n{}",response.headers());
        log.info("Status Code: {}", response.status());
        if(response.text()!=null && !response.text().isEmpty() && !response.text().isBlank()) {
            log.info("Response Body: \n{}", prettyPrintJson(response.text()));
        }
        
    }

    private String prettyPrintJson(String text) {
        String prettyPrintJson = "";
        if (text != null && !text.isBlank() && !text.isEmpty()) {
            try {

                ObjectMapper objectMapper = new ObjectMapper();
                Object jsonObject = objectMapper.readValue(text, Object.class);
                prettyPrintJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
                return prettyPrintJson;
            } catch (JsonProcessingException e) {
                log.error("Error Printing Pretty Json : {}", e.getMessage());
            }
        }
        
        log.info("No response body generated!");
        return null;

    }
}
