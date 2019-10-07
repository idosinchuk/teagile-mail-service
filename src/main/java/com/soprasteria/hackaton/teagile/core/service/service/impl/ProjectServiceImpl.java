package com.soprasteria.hackaton.teagile.core.service.service.impl;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import com.soprasteria.hackaton.teagile.core.service.controller.ProjectController;
import com.soprasteria.hackaton.teagile.core.service.controller.UserController;
import com.soprasteria.hackaton.teagile.core.service.dto.ProjectRequestDTO;
import com.soprasteria.hackaton.teagile.core.service.dto.ProjectResponseDTO;
import com.soprasteria.hackaton.teagile.core.service.dto.UserResponseDTO;
import com.soprasteria.hackaton.teagile.core.service.entity.ProjectEntity;
import com.soprasteria.hackaton.teagile.core.service.entity.UserEntity;
import com.soprasteria.hackaton.teagile.core.service.mail.MailClient;
import com.soprasteria.hackaton.teagile.core.service.repository.ProjectRepository;
import com.soprasteria.hackaton.teagile.core.service.repository.UserRepository;
import com.soprasteria.hackaton.teagile.core.service.service.ProjectService;

@Service("ProjectService")
public class ProjectServiceImpl implements ProjectService {

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	MailClient mailClient;

	public static final Logger logger = LoggerFactory.getLogger(ProjectServiceImpl.class);

	public ResponseEntity<?> getAllProjectsByUserId(int userId) {

		Resources<CustomMessage> resource = null;
		List<CustomMessage> customMessageList = null;
		List<ProjectResponseDTO> projects;

		try {

			Set<ProjectEntity> entityResponse = projectRepository.findByUsers_Id(userId);

			if (entityResponse.isEmpty()) {
				customMessageList = ArrayListCustomMessage.setMessage("There are not meetings!", HttpStatus.NO_CONTENT);
				resource = new Resources<>(customMessageList);
				resource.add(linkTo(ProjectController.class).withSelfRel());
				return new ResponseEntity<>(resource, HttpStatus.NO_CONTENT);
			}

			// Convert Set to List
			List<ProjectEntity> projectsEntity = new ArrayList<>(entityResponse);

			// Convert Entity response to DTO
			projects = modelMapper.map(projectsEntity, new TypeToken<List<ProjectResponseDTO>>() {
			}.getType());

		} catch (Exception e) {
			logger.error("An error occurred! {}", e.getMessage());
			return CustomErrorType.returnResponsEntityError(e.getMessage());
		}
		return new ResponseEntity<>(projects, HttpStatus.OK);

	}

	public ResponseEntity<?> getProjectByProjectIdAndUserId(int id, int userId) {

		ProjectResponseDTO projectResponseDTO = null;
		Resources<CustomMessage> resource = null;

		try {
			List<CustomMessage> customMessageList = null;

			ProjectEntity entityResponse = projectRepository.findById_AndUsers_Id(id, userId);

			if (entityResponse == null) {
				customMessageList = ArrayListCustomMessage
						.setMessage("The requested project does not exists. Please try again.", HttpStatus.NO_CONTENT);
				resource = new Resources<>(customMessageList);
				resource.add(linkTo(ProjectController.class).withSelfRel());
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			projectResponseDTO = modelMapper.map(entityResponse, ProjectResponseDTO.class);

		} catch (Exception e) {
			logger.error("An error occurred! {}", e.getMessage());
			return CustomErrorType.returnResponsEntityError(e.getMessage());
		}

		return new ResponseEntity<>(projectResponseDTO, HttpStatus.OK);

	}

	@Transactional
	public ResponseEntity<?> addProject(ProjectRequestDTO projectRequestDTO, int userId) {

		List<CustomMessage> customMessageList = null;
		Resources<CustomMessage> resource = null;
		ProjectResponseDTO projectResponseDTO = null;

		try {
			UserEntity userEntity = userRepository.findById(userId);

			// Check if userId exists in the database
			if (userEntity == null) {
				customMessageList = ArrayListCustomMessage.setMessage(
						"The requested userId does not exists. Please select correct user.", HttpStatus.BAD_REQUEST);
				resource = new Resources<>(customMessageList);
				resource.add(linkTo(ProjectController.class).withSelfRel());
				return new ResponseEntity<>(resource, HttpStatus.BAD_REQUEST);
			}

			ProjectEntity projectEntityRequest = modelMapper.map(projectRequestDTO, ProjectEntity.class);

			ProjectEntity projectEntityResponse = projectRepository.save(projectEntityRequest);

			// Add project to user
			userEntity.getProjects().add(projectEntityResponse);
			userRepository.save(userEntity);

			projectResponseDTO = modelMapper.map(projectEntityResponse, ProjectResponseDTO.class);
		} catch (Exception e) {
			logger.error("An error occurred! {}", e.getMessage());
			return CustomErrorType.returnResponsEntityError(e.getMessage());
		}

		return new ResponseEntity<>(projectResponseDTO, HttpStatus.CREATED);

	}

	public ResponseEntity<?> addUserToProject(int projectId, int userId) {

		UserResponseDTO userResponseDTO = null;
		Resources<CustomMessage> resource = null;

		try {
			UserEntity userEntity;
			ProjectEntity projectEntityResponse;

			List<CustomMessage> customMessageList = null;

			projectEntityResponse = projectRepository.findById(projectId);
			userEntity = userRepository.findById(userId);

			// If user and project exists, add project to user
			if (projectEntityResponse != null && userEntity != null) {
				// Convert Set to List
				List<ProjectEntity> projects = new ArrayList<>(userEntity.getProjects());
				projects.add(projectEntityResponse);
				// Convert List to Set
				Set<ProjectEntity> projectSet = new HashSet<>(projects);
				userEntity.setProjects(projectSet);

				userRepository.save(userEntity);

				customMessageList = ArrayListCustomMessage.setMessage("Added project to user", HttpStatus.CREATED);
				resource = new Resources<>(customMessageList);
				resource.add(linkTo(UserController.class).withSelfRel());

				String type = "User added to project";

				// TODO: Mandar mail con: Se te ha añadido al proyecto.
				mailClient.prepareAndSend(userEntity.getEmail(), type);

			}

			else {
				customMessageList = ArrayListCustomMessage.setMessage("The project or User does not exists.",
						HttpStatus.NO_CONTENT);
				resource = new Resources<>(customMessageList);
				resource.add(linkTo(UserController.class).withSelfRel());
				return new ResponseEntity<>(resource, HttpStatus.NO_CONTENT);
			}

		} catch (Exception e) {
			logger.error("An error occurred! {}", e.getMessage());
			return CustomErrorType.returnResponsEntityError(e.getMessage());
		}

		return new ResponseEntity<>(userResponseDTO, HttpStatus.OK);

	}

	@Transactional
	public ResponseEntity<?> updateProject(int projectId, ProjectRequestDTO projectRequestDTO) {

		List<CustomMessage> customMessageList = null;
		Resources<CustomMessage> resource = null;

		try {

			// Check if project exists in the database
			ProjectEntity projectEntity = projectRepository.findById(projectId);
			if (projectEntity == null) {
				customMessageList = ArrayListCustomMessage.setMessage("Project Id " + projectId + " Not Found!",
						HttpStatus.NO_CONTENT);
				resource = new Resources<>(customMessageList);
				resource.add(linkTo(ProjectController.class).withSelfRel());
				return new ResponseEntity<>(resource, HttpStatus.NO_CONTENT);
			}

			ProjectEntity entityRequest = modelMapper.map(projectRequestDTO, ProjectEntity.class);
			entityRequest.setId(projectId);

			projectRepository.save(entityRequest);

		} catch (Exception e) {
			logger.error("An error occurred! {}", e.getMessage());
			return CustomErrorType.returnResponsEntityError(e.getMessage());

		}

		return new ResponseEntity<>(HttpStatus.OK);

	}

	public ResponseEntity<?> deleteProject(int id) {

		try {
			projectRepository.deleteById(id);

		} catch (Exception e) {
			logger.error("An error occurred! {}", e.getMessage());
			return CustomErrorType.returnResponsEntityError(e.getMessage());
		}

		return new ResponseEntity<>(HttpStatus.OK);

	}
}
