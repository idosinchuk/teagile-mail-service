package com.soprasteria.hackaton.teagile.core.service.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.soprasteria.hackaton.teagile.core.service.dto.UserRequestDTO;
import com.soprasteria.hackaton.teagile.core.service.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "API Rest for User.")
@RequestMapping(value = "/api/v1")
public class UserController {

	public static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	UserService userService;

	@GetMapping(path = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ApiOperation(value = "Retrieve list of all users.")
	public ResponseEntity<?> getAllUsers() {

		logger.info("Fetching all users");
		return userService.getAllUsers();
	}

	@GetMapping(path = "/users/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Retrieve user by User ID.")
	public ResponseEntity<?> getUser(@PathVariable("userId") int userId) {

		logger.info("Fetching user with userId {} ", userId);
		return userService.getUser(userId);
	}
	
	@GetMapping(path = "/users/email/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Retrieve user by email.")
	public ResponseEntity<?> getUserByEmail(@PathVariable("email") String email) {

		logger.info("Fetching user with email {} ", email);
		return userService.getUserByEmail(email);
	}

	@GetMapping(path = "/users/login", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Retrieve user by login credentials.")
	public ResponseEntity<?> getUserByEmailAndPassword(@RequestParam("email") String email,
			@RequestParam("password") String password) {

		logger.info("Fetching user by email and password with email {} ", email);
		return userService.getUserByEmailAndPassword(email, password);

	}

	@PostMapping(path = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Add a user.")
	public ResponseEntity<?> addUser(@Valid @RequestBody UserRequestDTO userRequestDTO) {

		logger.info("Process add user");
		return userService.addUser(userRequestDTO);
	}

	@PatchMapping(path = "/users/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Update the user.")
	public ResponseEntity<?> updateUserByUserId(@PathVariable("userId") int userId, @RequestBody UserRequestDTO userRequestDTO) {

		logger.info("Process patch user");
		return userService.updateUser(userId, userRequestDTO);
	}

	@DeleteMapping(path = "/users/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Delete user by User ID.")
	public ResponseEntity<?> deleteUser(@PathVariable("userId") int userId) {

		logger.info("Deleting user with userId {} ", userId);
		return userService.deleteUser(userId);
	}
}
