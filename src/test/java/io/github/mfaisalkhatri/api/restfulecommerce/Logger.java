package io.github.mfaisalkhatri.api.restfulecommerce;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;


public class Logger {

    private final APIResponse                     response;
    private final org.apache.logging.log4j.Logger log;

    public Logger (final APIResponse response) {
        this.response = response;
        this.log = LogManager.getLogger (getClass ());
    }

    public void logResponseDetails() {
        String responseBody = this.response.text ();
        if (StringUtils.isNotBlank (responseBody)) {
            this.log.info ("Logging Response Details....\n responseHeaders: {}, \nstatusCode: {}, \nresponseBody: {}",
                this.response.headers (), this.response.status (), prettyPrintJson (this.response.text ()));
        }
        this.log.info ("End of Logs!");
    }

    private String prettyPrintJson (final String text) {
        try {
            final ObjectMapper objectMapper = new ObjectMapper ();
            final Object jsonObject = objectMapper.readValue (text, Object.class);
            return objectMapper.writerWithDefaultPrettyPrinter ()
                .writeValueAsString (jsonObject);
        } catch (final JsonProcessingException e) {
            this.log.error ("Failed to pretty print JSON: {}", e.getMessage (), e);
            return text;
        }
    }

    //    private String prettyPrintJson (final String text) {
    //        String prettyPrintJson = "";
    //        if (text != null && !text.isBlank() && !text.isEmpty()) {
    //            try {
    //
    //                final ObjectMapper objectMapper = new ObjectMapper ();
    //                final Object jsonObject = objectMapper.readValue (text, Object.class);
    //                prettyPrintJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
    //                return prettyPrintJson;
    //            } catch (final JsonProcessingException e) {
    //                this.log.error ("Error Printing Pretty Json : {}", e.getMessage ());
    //            }
    //        }
    //        this.log.info ("No response body generated!");
    //        return null;
    //    }
}
