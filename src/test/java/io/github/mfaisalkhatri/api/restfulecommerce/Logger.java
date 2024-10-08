package io.github.mfaisalkhatri.api.restfulecommerce;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIResponse;
import org.apache.logging.log4j.LogManager;


public class Logger {

    private final APIResponse                     response;
    private final org.apache.logging.log4j.Logger log;

    public Logger (final APIResponse response) {
        this.response = response;
        this.log = LogManager.getLogger (getClass ());
    }

    public void logResponseDetails() {
        this.log.trace ("Logging Response Details.....");

        this.log.info ("Response Headers: \n{}", this.response.headers ());
        this.log.info ("Status Code: {}", this.response.status ());
        if (this.response.text () != null && !this.response.text ()
            .isEmpty () && !this.response.text ()
            .isBlank ()) {
            this.log.info ("Response Body: \n{}", prettyPrintJson (this.response.text ()));
        }
        this.log.trace ("End of Logs!");
    }

    private String prettyPrintJson (final String text) {
        String prettyPrintJson = "";
        if (text != null && !text.isBlank() && !text.isEmpty()) {
            try {

                final ObjectMapper objectMapper = new ObjectMapper ();
                final Object jsonObject = objectMapper.readValue (text, Object.class);
                prettyPrintJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
                return prettyPrintJson;
            } catch (final JsonProcessingException e) {
                this.log.error ("Error Printing Pretty Json : {}", e.getMessage ());
            }
        }
        this.log.info ("No response body generated!");
        return null;

    }
}
