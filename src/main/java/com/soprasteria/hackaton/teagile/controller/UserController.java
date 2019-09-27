package com.soprasteria.hackaton.teagile.controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.soprasteria.hackaton.teagile.dto.UserRequestDTO;
import com.soprasteria.hackaton.teagile.dto.UserResponseDTO;
import com.soprasteria.hackaton.teagile.service.UserService;

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
public class UserController {

	public static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	UserService userService;

	/**
	 * Retrieve list of all users.
	 * 
	 * @param pageable paging fields
	 * @return ResponseEntity with paged list of all users, headers and status
	 */
	@GetMapping(path = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ApiOperation(value = "Retrieve list of all users.")
	public ResponseEntity<List<UserResponseDTO>> getAllUsers() {

		logger.info("Fetching all users");
		return userService.getAllUsers();
	}

	/**
	 * Retrieve user by Id.
	 * 
	 * @param id user Id
	 * @return ResponseEntity with status and userResponseDTO
	 */
	@GetMapping(path = "/users/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ApiOperation(value = "Retrieve user by Id.")
	public ResponseEntity<UserResponseDTO> getUser(@PathVariable("id") int id) {

		logger.info("Fetching user with id {} ", id);
		return userService.getUser(id);
	}

	/**
	 * Retrieve user by user email.
	 * 
	 * @param email user email
	 * @return ResponseEntity with status and userResponseDTO
	 */
	@GetMapping(path = "/users/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ApiOperation(value = "Retrieve user by email.")
	public ResponseEntity<UserResponseDTO> getUserByEmail(@PathVariable("email") String email) {

		logger.info("Fetching user with email {} ", email);
		return userService.getUserByEmail(email);
	}

	/**
	 * Retrieve user by email and password.
	 * 
	 * @param email    user email
	 * @param password user password
	 * @return ResponseEntity with status and userResponseDTO
	 */
	@GetMapping(path = "/users/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ApiOperation(value = "Retrieve user by login credentials.")
	public ResponseEntity<UserResponseDTO> getUserByLogin(@RequestParam("email") String email,
			@RequestParam("password") String password) {

		logger.info("Fetching user by email and password with email {} ", email);
		return userService.getUserByEmailAndPassword(email, password);

	}

	/**
	 * Add a user.
	 * 
	 * @param userRequestDTO object to save
	 * @return ResponseEntity with status and userResponseDTO
	 */
	@PostMapping(path = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ApiOperation(value = "Add a user.")
	public ResponseEntity<?> addUser(@Valid @RequestBody UserRequestDTO userRequestDTO) {

		logger.info("Process add user");
		return userService.addUser(userRequestDTO);
	}

	/**
	 * Update a user
	 * 
	 * @param id             user id
	 * @param userRequestDTO object to update
	 * @return ResponseEntity with resource and status
	 */
	@PatchMapping(path = "/users/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ApiOperation(value = "Update the user.")
	public ResponseEntity<?> updateUser(@PathVariable("id") int id, @RequestBody UserRequestDTO userRequestDTO) {

		logger.info("Process patch user");
		return userService.updateUser(id, userRequestDTO);
	}
}
