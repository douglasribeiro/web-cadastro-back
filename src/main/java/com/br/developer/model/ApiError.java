package com.br.developer.model;

import java.time.Instant;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiError {

	private int status;
	private String error;
	private String message;
	private String path;
	private Instant timestamp;
	
	public ApiError(int status, String error, String message, String path, Instant timestamp) {
		super();
		this.status = status;
		this.error = error;
		this.message = message;
		this.path = path;
		this.timestamp = timestamp;
	}
	
}
