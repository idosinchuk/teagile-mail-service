package com.soprasteria.hackaton.teagile.service.impl;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soprasteria.hackaton.teagile.common.ArrayListCustomMessage;
import com.soprasteria.hackaton.teagile.common.CustomErrorType;
import com.soprasteria.hackaton.teagile.common.CustomMessage;
import com.soprasteria.hackaton.teagile.controller.UserController;
import com.soprasteria.hackaton.teagile.dto.UserRequestDTO;
import com.soprasteria.hackaton.teagile.dto.UserResponseDTO;
import com.soprasteria.hackaton.teagile.entity.UserEntity;
import com.soprasteria.hackaton.teagile.repository.UserRepository;
import com.soprasteria.hackaton.teagile.service.UserService;

/**
 * Implementation for user service
 * 
 * @author Igor Dosinchuk
 *
 */
@Service("UserService")
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ModelMapper modelMapper;

	public static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	/**
	 * {@inheritDoc}
	 */
	public ResponseEntity<List<UserResponseDTO>> getAllUsers() {

		List<UserEntity> entityResponse = userRepository.findAll();

		// Convert Entity response to DTO
		List<UserResponseDTO> users = modelMapper.map(entityResponse, new TypeToken<List<UserResponseDTO>>() {
		}.getType());

		return new ResponseEntity<>(users, HttpStatus.OK);

	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public ResponseEntity<UserResponseDTO> getUser(int id) {

		UserResponseDTO userResponseDTO = null;
		Resources<CustomMessage> resource = null;

		try {
			List<CustomMessage> customMessageList = null;

			UserEntity entityResponse = userRepository.findById(id);

			if (entityResponse == null) {
				customMessageList = ArrayListCustomMessage
						.setMessage("The requested user does not exists. Please try again.", HttpStatus.NO_CONTENT);
				resource = new Resources<>(customMessageList);
				resource.add(linkTo(UserController.class).withSelfRel());

				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			userResponseDTO = modelMapper.map(entityResponse, UserResponseDTO.class);

		} catch (Exception e) {
			logger.error("An error occurred! {}", e.getMessage());
			return CustomErrorType.returnResponsEntityError(e.getMessage());
		}

		return new ResponseEntity<>(userResponseDTO, HttpStatus.OK);

	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public ResponseEntity<UserResponseDTO> getUserByEmail(String email) {

		UserResponseDTO userResponseDTO = null;
		Resources<CustomMessage> resource = null;

		try {
			List<CustomMessage> customMessageList = null;

			UserEntity entityResponse = userRepository.findByEmail(email);

			if (entityResponse == null) {
				customMessageList = ArrayListCustomMessage
						.setMessage("The requested user does not exists. Please try again.", HttpStatus.NO_CONTENT);
				resource = new Resources<>(customMessageList);
				resource.add(linkTo(UserController.class).withSelfRel());

				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			userResponseDTO = modelMapper.map(entityResponse, UserResponseDTO.class);

		} catch (Exception e) {
			logger.error("An error occurred! {}", e.getMessage());
			return CustomErrorType.returnResponsEntityError(e.getMessage());
		}

		return new ResponseEntity<>(userResponseDTO, HttpStatus.OK);

	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public ResponseEntity<UserResponseDTO> getUserByEmailAndPassword(String email, String password) {

		UserResponseDTO userResponseDTO = null;
		Resources<CustomMessage> resource = null;

		try {
			List<CustomMessage> customMessageList = null;

			UserEntity entityResponse = userRepository.findByEmailAndPassword(email, password);

			if (entityResponse == null) {
				customMessageList = ArrayListCustomMessage
						.setMessage("The requested user does not exists. Please try again.", HttpStatus.NO_CONTENT);
				resource = new Resources<>(customMessageList);
				resource.add(linkTo(UserController.class).withSelfRel());

				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			userResponseDTO = modelMapper.map(entityResponse, UserResponseDTO.class);

		} catch (Exception e) {
			logger.error("An error occurred! {}", e.getMessage());
			return CustomErrorType.returnResponsEntityError(e.getMessage());
		}

		return new ResponseEntity<>(userResponseDTO, HttpStatus.OK);

	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional
	public ResponseEntity<?> addUser(UserRequestDTO userRequestDTO) {

		Resources<CustomMessage> resource = null;

		try {
			List<CustomMessage> customMessageList = null;

			UserEntity entityRequest = modelMapper.map(userRequestDTO, UserEntity.class);

			UserEntity userEntity = userRepository.findByEmail(userRequestDTO.getEmail());

			// Check if email exists in the database
			if (userEntity != null) {
				customMessageList = ArrayListCustomMessage.setMessage(
						"The requested user actually exists. Please change the email.", HttpStatus.BAD_REQUEST);
				resource = new Resources<>(customMessageList);
				resource.add(linkTo(UserController.class).withSelfRel());

				return new ResponseEntity<>(resource, HttpStatus.BAD_REQUEST);
			}

			userRepository.save(entityRequest);

			customMessageList = ArrayListCustomMessage.setMessage("Created new user", HttpStatus.CREATED);

			resource = new Resources<>(customMessageList);
			resource.add(linkTo(UserController.class).withSelfRel());
		} catch (Exception e) {
			logger.error("An error occurred! {}", e.getMessage());
			return CustomErrorType.returnResponsEntityError(e.getMessage());
		}

		return new ResponseEntity<>(resource, HttpStatus.OK);

	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional
	public ResponseEntity<?> updateUser(int id, UserRequestDTO userRequestDTO) {

		Resources<CustomMessage> resource = null;

		try {

			List<CustomMessage> customMessageList = null;

			customMessageList = ArrayListCustomMessage.setMessage("Patch user process", HttpStatus.OK);

			// Check if request is null
			if (userRequestDTO == null) {
				customMessageList = ArrayListCustomMessage.setMessage("Request body is null!", HttpStatus.BAD_REQUEST);
				resource = new Resources<>(customMessageList);
				resource.add(linkTo(UserController.class).withSelfRel());
				return new ResponseEntity<>(resource, HttpStatus.BAD_REQUEST);
			}

			// Find user by ID for check if exists in DB
			UserEntity userEntity = userRepository.findById(id);

			// If exists
			if (userEntity != null) {

				// The user ID will always be the same, so we do not allow it to
				// be updated, for them we overwrite the field with the original value.
				userRequestDTO.setId(userEntity.getId());

				UserEntity entityRequest = modelMapper.map(userRequestDTO, UserEntity.class);
				userRepository.save(entityRequest);

			} else {
				customMessageList = ArrayListCustomMessage.setMessage("User id " + id + " Not Found!",
						HttpStatus.BAD_REQUEST);
				resource = new Resources<>(customMessageList);
				resource.add(linkTo(UserController.class).withSelfRel());
				return new ResponseEntity<>(resource, HttpStatus.BAD_REQUEST);
			}

			resource = new Resources<>(customMessageList);
			resource.add(linkTo(UserController.class).slash(userRequestDTO.getId()).withSelfRel());
		} catch (Exception e) {
			logger.error("An error occurred! {}", e.getMessage());
			return CustomErrorType.returnResponsEntityError(e.getMessage());

		}

		return new ResponseEntity<>(resource, HttpStatus.OK);

	}
}
