package com.soprasteria.hackaton.teagile.core.service.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import lombok.Data;

@Data
public class CustomErrorType {

	public static ResponseEntity<?> returnResponsEntityError(String message) {
		return new ResponseEntity<>("An error occurred: " + message,
				HttpStatus.INTERNAL_SERVER_ERROR);

	}
}