package com.soprasteria.hackaton.teagile.core.service.service.impl;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import java.util.ArrayList;
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
import com.soprasteria.hackaton.teagile.core.service.controller.ProjectController;
import com.soprasteria.hackaton.teagile.core.service.dto.ProjectRequestDTO;
import com.soprasteria.hackaton.teagile.core.service.dto.ProjectResponseDTO;
import com.soprasteria.hackaton.teagile.core.service.entity.ProjectEntity;
import com.soprasteria.hackaton.teagile.core.service.entity.UserEntity;
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

	public static final Logger logger = LoggerFactory.getLogger(ProjectServiceImpl.class);

	public ResponseEntity<?> getAllProjectsByUserId(int userId) {

		Resources<CustomMessage> resource = null;
		List<CustomMessage> customMessageList = null;
		List<ProjectResponseDTO> projects = new ArrayList<>();
		
		try {
			
		UserEntity entityResponse = userRepository.findById(userId);

		if (entityResponse.getProjects().isEmpty()) {
			customMessageList = ArrayListCustomMessage.setMessage("There are not meetings!",
					HttpStatus.NO_CONTENT);
			resource = new Resources<>(customMessageList);
			resource.add(linkTo(ProjectController.class).withSelfRel());
			return new ResponseEntity<>(resource, HttpStatus.NO_CONTENT);
		}
		
		// Convert Entity response to DTO
		projects = modelMapper.map(entityResponse.getProjects(),
				new TypeToken<List<ProjectResponseDTO>>() {
				}.getType());
		
		} catch (Exception e){
			logger.error("An error occurred! {}", e.getMessage());
			return CustomErrorType.returnResponsEntityError(e.getMessage());
		}
		return new ResponseEntity<>(projects, HttpStatus.OK);

	}

	public ResponseEntity<?> getProject(int id) {

		ProjectResponseDTO projectResponseDTO = null;
		Resources<CustomMessage> resource = null;

		try {
			List<CustomMessage> customMessageList = null;

			ProjectEntity entityResponse = projectRepository.findById(id);

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

		Resources<CustomMessage> resource = null;

		try {
			List<CustomMessage> customMessageList = null;

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

			// If project was created successfully, add project to user
			if (projectEntityResponse != null) {
				userEntity.getProjects().add(projectEntityResponse);
				userRepository.save(userEntity);
			}

			customMessageList = ArrayListCustomMessage.setMessage("Created new project", HttpStatus.CREATED);

			resource = new Resources<>(customMessageList);
			resource.add(linkTo(ProjectController.class).withSelfRel());
		} catch (Exception e) {
			logger.error("An error occurred! {}", e.getMessage());
			return CustomErrorType.returnResponsEntityError(e.getMessage());
		}

		return new ResponseEntity<>(resource, HttpStatus.OK);

	}

	@Transactional
	public ResponseEntity<?> updateProject(int id, ProjectRequestDTO projectRequestDTO) {

		Resources<CustomMessage> resource = null;

		try {

			List<CustomMessage> customMessageList = null;

			customMessageList = ArrayListCustomMessage.setMessage("Patch project process", HttpStatus.OK);

			// Find project by ID for check if exists in DB
			ProjectEntity projectEntity = projectRepository.findById(id);

			// If exists
			if (projectEntity != null) {

				// The project ID will always be the same, so we do not allow it to
				// be updated, for them we overwrite the field with the original value.
				projectRequestDTO.setId(projectEntity.getId());

				ProjectEntity entityRequest = modelMapper.map(projectRequestDTO, ProjectEntity.class);
				projectRepository.save(entityRequest);

			} else {
				customMessageList = ArrayListCustomMessage.setMessage("Project id " + id + " Not Found!",
						HttpStatus.BAD_REQUEST);
				resource = new Resources<>(customMessageList);
				resource.add(linkTo(ProjectController.class).withSelfRel());
				return new ResponseEntity<>(resource, HttpStatus.BAD_REQUEST);
			}

			resource = new Resources<>(customMessageList);
			resource.add(linkTo(ProjectController.class).slash(projectRequestDTO.getId()).withSelfRel());
		} catch (Exception e) {
			logger.error("An error occurred! {}", e.getMessage());
			return CustomErrorType.returnResponsEntityError(e.getMessage());

		}

		return new ResponseEntity<>(resource, HttpStatus.OK);

	}

	public ResponseEntity<?> deleteProject(int id) {

		ProjectResponseDTO projectResponseDTO = null;

		try {
			projectRepository.deleteById(id);

		} catch (Exception e) {
			logger.error("An error occurred! {}", e.getMessage());
			return CustomErrorType.returnResponsEntityError(e.getMessage());
		}

		return new ResponseEntity<>(projectResponseDTO, HttpStatus.OK);

	}
}
