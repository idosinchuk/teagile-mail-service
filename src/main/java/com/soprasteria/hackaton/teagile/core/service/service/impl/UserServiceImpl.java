package com.soprasteria.hackaton.teagile.core.service.service.impl;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import java.util.ArrayList;
import java.util.Base64;
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

import com.soprasteria.hackaton.teagile.core.service.common.ArrayListCustomMessage;
import com.soprasteria.hackaton.teagile.core.service.common.CustomErrorType;
import com.soprasteria.hackaton.teagile.core.service.common.CustomMessage;
import com.soprasteria.hackaton.teagile.core.service.controller.MeetingController;
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

	public ResponseEntity<?> getAllUsers() {

		Resources<CustomMessage> resource = null;
		List<CustomMessage> customMessageList = null;
		List<UserResponseDTO> users = new ArrayList<>();
		
		try {
			List<UserEntity> entityResponse = userRepository.findAll();

			if (!entityResponse.isEmpty()) {
				customMessageList = ArrayListCustomMessage.setMessage("There are not meetings!",
						HttpStatus.NO_CONTENT);
				resource = new Resources<>(customMessageList);
				resource.add(linkTo(MeetingController.class).withSelfRel());
				return new ResponseEntity<>(resource, HttpStatus.NO_CONTENT);
			}

			// Convert Entity response to DTO
			users = modelMapper.map(entityResponse, new TypeToken<List<UserResponseDTO>>() {
			}.getType());

		} catch (Exception e) {
			logger.error("An error occurred! {}", e.getMessage());
			return CustomErrorType.returnResponsEntityError(e.getMessage());
		}

		return new ResponseEntity<>(users, HttpStatus.OK);

	}

	public ResponseEntity<?> getUser(int id) {

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

				return new ResponseEntity<>(resource, HttpStatus.NO_CONTENT);
			}

			userResponseDTO = modelMapper.map(entityResponse, UserResponseDTO.class);

		} catch (Exception e) {
			logger.error("An error occurred! {}", e.getMessage());
			return CustomErrorType.returnResponsEntityError(e.getMessage());
		}

		return new ResponseEntity<>(userResponseDTO, HttpStatus.OK);

	}

	public ResponseEntity<?> getUserByEmail(String email) {

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

	public ResponseEntity<?> getUserByEmailAndPassword(String email, String password) {

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

	public ResponseEntity<?> addUserToProject(int projectId, int userId) {

		UserResponseDTO userResponseDTO = null;
		Resources<CustomMessage> resource = null;

		try {
			UserEntity userEntity;
			ProjectEntity projectEntityResponse;

			List<CustomMessage> customMessageList = null;

			// Check if project exists in db
			projectEntityResponse = projectRepository.findById(projectId);

			// If project does not exists in db
			if (projectEntityResponse == null) {
				customMessageList = ArrayListCustomMessage.setMessage(
						"The project does not exists. Please try to add to other project.", HttpStatus.NO_CONTENT);
				resource = new Resources<>(customMessageList);
				resource.add(linkTo(UserController.class).withSelfRel());

				return new ResponseEntity<>(resource, HttpStatus.NO_CONTENT);
			}

			// Check if user exists in db
			userEntity = userRepository.findById(userId);

			// If user exists, add project to user
			if (userEntity != null) {
				List<ProjectEntity> projects;
				projects = userEntity.getProjects();
				projects.add(projectEntityResponse);
				userEntity.setProjects(projects);

				userRepository.save(userEntity);

				customMessageList = ArrayListCustomMessage.setMessage("Added project to user", HttpStatus.CREATED);

				resource = new Resources<>(customMessageList);
				resource.add(linkTo(UserController.class).withSelfRel());

				String type = "User added to project";

				// TODO: Mandar mail con: Se te ha añadido al proyecto.
				mailClient.prepareAndSend(userEntity.getEmail(), type);

			}

			else {
				return new ResponseEntity<>(userResponseDTO, HttpStatus.NO_CONTENT);

			}

		} catch (Exception e) {
			logger.error("An error occurred! {}", e.getMessage());
			return CustomErrorType.returnResponsEntityError(e.getMessage());
		}

		return new ResponseEntity<>(userResponseDTO, HttpStatus.OK);

	}

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
						"The requested user actually exists. Please try with other email.", HttpStatus.NO_CONTENT);
				resource = new Resources<>(customMessageList);
				resource.add(linkTo(UserController.class).withSelfRel());

				return new ResponseEntity<>(resource, HttpStatus.NO_CONTENT);
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

	@Transactional
	public ResponseEntity<?> updateUser(int userId, UserRequestDTO userRequestDTO) {

		Resources<CustomMessage> resource = null;

		try {

			List<CustomMessage> customMessageList = null;

			customMessageList = ArrayListCustomMessage.setMessage("Patch user process", HttpStatus.OK);

			// Check if request is null
			if (userRequestDTO == null) {
				customMessageList = ArrayListCustomMessage.setMessage("Request body is null!", HttpStatus.BAD_REQUEST);
				resource = new Resources<>(customMessageList);
				resource.add(linkTo(UserController.class).withSelfRel());
				return new ResponseEntity<>(resource, HttpStatus.NO_CONTENT);
			}

			// Find user by ID for check if exists in DB
			UserEntity userEntity = userRepository.findById(userId);

			// If exists
			if (userEntity != null) {

				UserEntity entityRequest = modelMapper.map(userRequestDTO, UserEntity.class);

				// The user ID and email will always be the same, so we do not allow it to
				// be updated, for them we overwrite the field with the original value.
				entityRequest.setId(userEntity.getId());
				entityRequest.setEmail(userEntity.getEmail());

				userRepository.save(entityRequest);

			} else {
				customMessageList = ArrayListCustomMessage.setMessage("User id " + userId + " Not Found!",
						HttpStatus.NO_CONTENT);
				resource = new Resources<>(customMessageList);
				resource.add(linkTo(UserController.class).withSelfRel());
				return new ResponseEntity<>(resource, HttpStatus.NO_CONTENT);
			}

			resource = new Resources<>(customMessageList);
			resource.add(linkTo(UserController.class).slash(userId).withSelfRel());
		} catch (Exception e) {
			logger.error("An error occurred! {}", e.getMessage());
			return CustomErrorType.returnResponsEntityError(e.getMessage());

		}

		return new ResponseEntity<>(resource, HttpStatus.OK);

	}

	public ResponseEntity<?> deleteUser(int userId) {

		UserResponseDTO userResponseDTO = null;

		try {
			userRepository.deleteById(userId);

		} catch (Exception e) {
			logger.error("An error occurred! {}", e.getMessage());
			return CustomErrorType.returnResponsEntityError(e.getMessage());
		}

		return new ResponseEntity<>(userResponseDTO, HttpStatus.OK);

	}
}
