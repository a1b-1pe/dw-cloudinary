package com.a1b.dw.cloudinary.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeleteR {
private long id;
private boolean isdeleted;
private String deleteMessage;
private boolean isenabled;
private boolean isdisabled;

public long getId() {
	return id;
}
public void setId(long id) {
	this.id = id;
}
public boolean isIsdeleted() {
	return isdeleted;
}
public void setIsdeleted(boolean isdeleted) {
	this.isdeleted = isdeleted;
}
public String getDeleteMessage() {
	return deleteMessage;
}
public void setDeleteMessage(String deleteMessage) {
	this.deleteMessage = deleteMessage;
}
public boolean isIsenabled() {
	return isenabled;
}
public void setIsenabled(boolean isenabled) {
	this.isenabled = isenabled;
}
public boolean isIsdisabled() {
	return isdisabled;
}
public void setIsdisabled(boolean isdisabled) {
	this.isdisabled = isdisabled;
}


}
