package com.soprasteria.hackaton.teagile.core.service.service;

import org.springframework.http.ResponseEntity;

import com.soprasteria.hackaton.teagile.core.service.dto.UserResponseDTO;

/**
 * 
 * Service for login
 * 
 * @author Igor Dosinchuk
 *
 */
public interface LoginService {

	/**
	 * Check login by loginName and loginPassword.
	 * 
	 * @param loginName     user login name
	 * @param loginPassword user login password
	 * @return {@link UserResponseDTO}
	 */
	ResponseEntity<?> getLogin(String loginName, String loginPassword);

}