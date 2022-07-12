package com.emlcoding.springboot.backend.apirest.models.entity;

public class CustomError {

	private boolean error;
	private String reason;
	
	public boolean isError() {
		return error;
	}
	public void setError(boolean error) {
		this.error = error;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
}
