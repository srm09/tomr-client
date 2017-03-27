package main.java.edu.tomr.client.model;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by muchhals on 3/26/17.
 */
public class GenerateResponse {

    @JsonProperty("length")
    private int length;

    public GenerateResponse(int length) {
        this.length = length;
    }

    public GenerateResponse() {
    }
}
