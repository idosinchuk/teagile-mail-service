package com.soprasteria.hackaton.teagile.core.service.service;

import org.springframework.http.ResponseEntity;

/**
 * 
 * Service for login
 * 
 * @author Igor Dosinchuk
 *
 */
public interface LoginService {

	ResponseEntity<?> getLogin(String loginName, String loginPassword);

}
