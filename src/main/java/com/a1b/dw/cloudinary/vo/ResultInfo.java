package com.a1b.dw.cloudinary.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResultInfo {
    private String resultStatus;
    private String resultCode;
    private String resultMsg;

    @JsonProperty("resultStatus")
    public String getResultStatus() { return resultStatus; }
    @JsonProperty("resultStatus")
    public void setResultStatus(String value) { this.resultStatus = value; }

    @JsonProperty("resultCode")
    public String getResultCode() { return resultCode; }
    @JsonProperty("resultCode")
    public void setResultCode(String value) { this.resultCode = value; }

    @JsonProperty("resultMsg")
    public String getResultMsg() { return resultMsg; }
    @JsonProperty("resultMsg")
    public void setResultMsg(String value) { this.resultMsg = value; }
}
