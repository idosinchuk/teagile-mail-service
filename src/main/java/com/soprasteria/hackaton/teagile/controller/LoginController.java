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

import com.soprasteria.hackaton.teagile.dto.UserResponseDTO;
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
	 * Check login by loginName and loginPassword.
	 * 
	 * @param loginName     user login name
	 * @param loginPassword user login password
	 * @return ResponseEntity with status and userResponseDTO
	 */
	@GetMapping(path = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ApiOperation(value = "Retrieve user by login credentials.")
	public ResponseEntity<?> getLogin(@RequestParam("loginName") String loginName,
			@RequestParam("loginPassword") String loginPassword) {

		logger.info("Fetching user by loginName and LoginPassword with loginName {} ", loginName);
		return loginService.getLogin(loginName, loginPassword);

	}

	/**
	 * Retrieve user by loginName and loginPassword.
	 * 
	 * @param loginName     user login name
	 * @param loginPassword user login password
	 * @return ResponseEntity with status and userResponseDTO
	 */
	@GetMapping(path = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ApiOperation(value = "Retrieve user by login credentials.")
	public ResponseEntity<UserResponseDTO> getUserByLogin(@RequestParam("loginName") String loginName,
			@RequestParam("loginPassword") String loginPassword) {

		logger.info("Fetching user by loginName and LoginPassword with loginName {} ", loginName);
		return loginService.getUserByLogin(loginName, loginPassword);

	}
}
