package com.a1b.dw.cloudinary.vo;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Head {
    private Object requestID;
    private String responseTimestamp;
    private String version;

    @JsonProperty("requestId")
    public Object getRequestID() { return requestID; }
    @JsonProperty("requestId")
    public void setRequestID(Object value) { this.requestID = value; }

    @JsonProperty("responseTimestamp")
    public String getResponseTimestamp() { return responseTimestamp; }
    @JsonProperty("responseTimestamp")
    public void setResponseTimestamp(String value) { this.responseTimestamp = value; }

    @JsonProperty("version")
    public String getVersion() { return version; }
    @JsonProperty("version")
    public void setVersion(String value) { this.version = value; }
}