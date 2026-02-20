package com.a1b.dw.cloudinary.vo;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Body {
	private String data;
    private Object extraParamsMap;
    private ResultInfo resultInfo;

    @JsonProperty("data")
    public String getData() { return data; }
    @JsonProperty("data")
	public void setData(String data) { this.data = data; }
	@JsonProperty("extraParamsMap")
    public Object getExtraParamsMap() { return extraParamsMap; }
    @JsonProperty("extraParamsMap")
    public void setExtraParamsMap(Object value) { this.extraParamsMap = value; }
    @JsonProperty("resultInfo")
    public ResultInfo getResultInfo() { return resultInfo; }
    @JsonProperty("resultInfo")
    public void setResultInfo(ResultInfo value) { this.resultInfo = value; }
}