package com.soprasteria.hackaton.teagile.core.service.service.impl;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.apache.commons.lang.RandomStringUtils;
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

import com.soprasteria.hackaton.teagile.core.service.common.ArrayListCustomMessage;
import com.soprasteria.hackaton.teagile.core.service.common.CustomErrorType;
import com.soprasteria.hackaton.teagile.core.service.common.CustomMessage;
import com.soprasteria.hackaton.teagile.core.service.controller.UserController;
import com.soprasteria.hackaton.teagile.core.service.dto.UserRequestDTO;
import com.soprasteria.hackaton.teagile.core.service.dto.UserResponseDTO;
import com.soprasteria.hackaton.teagile.core.service.entity.ProjectEntity;
import com.soprasteria.hackaton.teagile.core.service.entity.UserEntity;
import com.soprasteria.hackaton.teagile.core.service.mail.MailClient;
import com.soprasteria.hackaton.teagile.core.service.repository.ProjectRepository;
import com.soprasteria.hackaton.teagile.core.service.repository.UserRepository;
import com.soprasteria.hackaton.teagile.core.service.service.UserService;

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
	private ProjectRepository projectRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	MailClient mailClient;

	public static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	/**
	 * {@inheritDoc}
	 */
	public ResponseEntity<List<UserResponseDTO>> getAllUsers() {

		List<UserEntity> entityResponse = userRepository.findAll();

		if (entityResponse == null) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}

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

			byte[] passwordBytes = Base64.getDecoder().decode(password);
			String decodedPassword = new String(passwordBytes);

			UserEntity entityResponse = userRepository.findByEmailAndPassword(email, decodedPassword);

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
	public ResponseEntity<?> addUserToProject(UserRequestDTO userRequestDTO, int projectId) {

		UserResponseDTO userResponseDTO = null;
		Resources<CustomMessage> resource = null;
		UserEntity userEntityResponse = null;

		try {
			UserEntity userEntity;
			ProjectEntity projectEntityResponse;

			List<CustomMessage> customMessageList = null;

			// Check if project exists in db
			projectEntityResponse = projectRepository.findById(projectId);

			// If project exists in db
			if (projectEntityResponse == null) {
				customMessageList = ArrayListCustomMessage.setMessage(
						"The project does not exists. Please try to add to other project.", HttpStatus.BAD_REQUEST);
				resource = new Resources<>(customMessageList);
				resource.add(linkTo(UserController.class).withSelfRel());

				return new ResponseEntity<>(userResponseDTO, HttpStatus.BAD_REQUEST);
			}

			// Check if user exists in db
			userEntity = userRepository.findByEmail(userRequestDTO.getEmail());

			// If user exists, add project to user
			if (userEntity != null) {
				List<ProjectEntity> projects = new ArrayList<>();
				projects = userEntity.getProjects();
				projects.add(projectEntityResponse);
				userEntity.setProjects(projects);

				userRepository.save(userEntity);

				customMessageList = ArrayListCustomMessage.setMessage("Added project to user", HttpStatus.CREATED);

				resource = new Resources<>(customMessageList);
				resource.add(linkTo(UserController.class).withSelfRel());

				// TODO: Mandar mail con: Se te ha añadido al proyecto.

			}

			// If user not exists, create user.
			else {
				// Convert userRequestDTO to userEntityRequest
				UserEntity userEntityRequest = modelMapper.map(userRequestDTO, UserEntity.class);

				List<ProjectEntity> projects = new ArrayList<>();
				projects.add(projectEntityResponse);
				// set project to new user
				userEntityRequest.setProjects(projects);
				// Set default password
				userEntityRequest.setPassword(RandomStringUtils.randomAlphabetic(10));
				userEntityResponse = userRepository.save(userEntityRequest);

				// Convert userRequestDTO to userEntityRequest
				userResponseDTO = modelMapper.map(userEntityResponse, UserResponseDTO.class);

				String type = "RegistrationWelcome";

				// Send email
				mailClient.prepareAndSend(userRequestDTO.getEmail(), type);

				customMessageList = ArrayListCustomMessage.setMessage("Created new user and added to the project",
						HttpStatus.CREATED);
				resource = new Resources<>(customMessageList);
				resource.add(linkTo(UserController.class).withSelfRel());
			}

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

			byte[] passwordBytes = Base64.getDecoder().decode(userRequestDTO.getPassword());
			String decodedPassword = new String(passwordBytes);
			entityRequest.setPassword(decodedPassword);

			userRepository.save(entityRequest);

			String type = "RegistrationWelcome";

			// Send email
			mailClient.prepareAndSend(userRequestDTO.getEmail(), type);

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

				// The user ID and email will always be the same, so we do not allow it to
				// be updated, for them we overwrite the field with the original value.
				userRequestDTO.setId(userEntity.getId());
				userRequestDTO.setEmail(userEntity.getEmail());

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

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public ResponseEntity<UserResponseDTO> deleteUser(int id) {

		UserResponseDTO userResponseDTO = null;

		try {
			userRepository.deleteById(id);

		} catch (Exception e) {
			logger.error("An error occurred! {}", e.getMessage());
			return CustomErrorType.returnResponsEntityError(e.getMessage());
		}

		return new ResponseEntity<>(userResponseDTO, HttpStatus.OK);

	}
}
