package com.soprasteria.hackaton.teagile.controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.idosinchuk.architecturechallenge.insurancecompany.util.CustomErrorType;
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

		List<UserResponseDTO> user = null;

		try {
			user = userService.getAllUsers();

		} catch (Exception e) {
			logger.error("An error occurred! {}", e.getMessage());
			return CustomErrorType.returnResponsEntityError(e.getMessage());
		}

		return new ResponseEntity<>(user, HttpStatus.OK);
	}

	/**
	 * Retrieve user by the userCode.
	 * 
	 * @param userCode user code
	 * @return ResponseEntity with status and userResponseDTO
	 */
	@GetMapping(path = "/users/{userCode}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ApiOperation(value = "Retrieve user by the userCode.")
	public ResponseEntity<UserResponseDTO> getUsers(@PathVariable("userCode") String userCode) {

		logger.info("Fetching user with userCode {}", userCode);

		UserResponseDTO userResponseDTO = null;

		try {
			// Search product in BD by userCode
			userResponseDTO = userService.getUsers(userCode);

			return new ResponseEntity<>(userResponseDTO, HttpStatus.OK);

		} catch (Exception e) {
			logger.error("An error occurred! {}", e.getMessage());
//			return new ResponseEntity<>(e.getMessage(),
//					HttpStatus.INTERNAL_SERVER_ERROR);

			return CustomErrorType.returnResponsEntityError(e.getMessage());

		}
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
	public ResponseEntity<?> addUsers(@Valid @RequestBody UserRequestDTO userRequestDTO) {

		logger.info("Process add user");

		UserResponseDTO userResponseDTO = userService.addUser(userRequestDTO);
		return new ResponseEntity<>(userResponseDTO, HttpStatus.OK);

	}

	/**
	 * Update a user
	 * 
	 * @param userCode       user code
	 * @param userRequestDTO object to update
	 * @return ResponseEntity with resource and status
	 */
	@PatchMapping(path = "/users/{userCode}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ApiOperation(value = "Update the user.")
	public ResponseEntity<?> updateUsers(@PathVariable("userCode") String userCode,
			@RequestBody UserRequestDTO userRequestDTO) {

		logger.info("Process patch user");

		UserResponseDTO userResponseDTO = userService.updateUser(userCode, userRequestDTO);

		return new ResponseEntity<>(userResponseDTO, HttpStatus.OK);
	}
}
