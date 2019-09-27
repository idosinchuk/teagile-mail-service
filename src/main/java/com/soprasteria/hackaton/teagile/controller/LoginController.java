package com.soprasteria.hackaton.teagile.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.soprasteria.hackaton.teagile.service.LoginService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * Controller for user
 * 
 * @author Igor Dosinchuk
 * @author Luis Rapestre
 * 
 */
@RestController
@Api(value = "API Rest for insurance users.")
@RequestMapping(value = "/api/v1")
public class LoginController {

	public static final Logger logger = LoggerFactory.getLogger(LoginController.class);

	@Autowired
	LoginService loginService;

	/**
	 * Check login by email and password.
	 * 
	 * @param email    user email
	 * @param password user password
	 * @return ResponseEntity with status and userResponseDTO
	 */
	@GetMapping(path = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ApiOperation(value = "Retrieve user by login credentials.")
	public ResponseEntity<?> getLogin(@RequestParam("email") String email, @RequestParam("password") String password) {

		logger.info("Fetching user by email and password with email {} ", email);
		return loginService.getLogin(email, password);

	}
}
