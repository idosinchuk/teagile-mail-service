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
import com.soprasteria.hackaton.teagile.controller.ProjectController;
import com.soprasteria.hackaton.teagile.dto.ProjectRequestDTO;
import com.soprasteria.hackaton.teagile.dto.ProjectResponseDTO;
import com.soprasteria.hackaton.teagile.entity.ProjectEntity;
import com.soprasteria.hackaton.teagile.repository.ProjectRepository;
import com.soprasteria.hackaton.teagile.service.ProjectService;

/**
 * Implementation for project service
 * 
 * @author Igor Dosinchuk
 *
 */
@Service("ProjectService")
public class ProjectServiceImpl implements ProjectService {

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private ModelMapper modelMapper;

	public static final Logger logger = LoggerFactory.getLogger(ProjectServiceImpl.class);

	/**
	 * {@inheritDoc}
	 */
	public List<ProjectResponseDTO> getAllProjects() {

		List<ProjectEntity> entityResponse = projectRepository.findAll();

		// Convert Entity response to DTO
		return modelMapper.map(entityResponse, new TypeToken<List<ProjectResponseDTO>>() {
		}.getType());
	}

	/**
	 * {@inheritDoc}
	 */
	public ProjectResponseDTO getProject(int id) {

		ProjectEntity entityResponse = projectRepository.findById(id);

		return modelMapper.map(entityResponse, ProjectResponseDTO.class);

	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional
	public ResponseEntity<?> addProject(ProjectRequestDTO projectRequestDTO) {

		Resources<CustomMessage> resource = null;

		try {
			List<CustomMessage> customMessageList = null;

			ProjectEntity entityRequest = modelMapper.map(projectRequestDTO, ProjectEntity.class);

			ProjectEntity projectEntity = projectRepository.findByName(projectRequestDTO.getName());

			// Check if projectName exists in the database
			if (projectEntity != null) {
				customMessageList = ArrayListCustomMessage.setMessage(
						"The requested project actually exists. Please change the project name.",
						HttpStatus.BAD_REQUEST);
				resource = new Resources<>(customMessageList);
				resource.add(linkTo(ProjectController.class).withSelfRel());

				return new ResponseEntity<>(resource, HttpStatus.BAD_REQUEST);
			}

			projectRepository.save(entityRequest);

			customMessageList = ArrayListCustomMessage.setMessage("Created new project", HttpStatus.CREATED);

			resource = new Resources<>(customMessageList);
			resource.add(linkTo(ProjectController.class).withSelfRel());
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
	public ResponseEntity<?> updateProject(int id, ProjectRequestDTO projectRequestDTO) {

		Resources<CustomMessage> resource = null;

		try {

			List<CustomMessage> customMessageList = null;

			customMessageList = ArrayListCustomMessage.setMessage("Patch project process", HttpStatus.OK);

			// Check if request is null
			if (projectRequestDTO == null) {
				customMessageList = ArrayListCustomMessage.setMessage("Request body is null!", HttpStatus.BAD_REQUEST);
				resource = new Resources<>(customMessageList);
				resource.add(linkTo(ProjectController.class).withSelfRel());
				return new ResponseEntity<>(resource, HttpStatus.BAD_REQUEST);
			}

			// Find project by ID for check if exists in DB
			ProjectEntity projectEntity = projectRepository.findById(id);

			// If exists
			if (projectEntity != null) {

				// The project ID will always be the same, so we do not allow it to
				// be updated, for them we overwrite the field with the original value.
				projectRequestDTO.setId(projectEntity.getId());

				ProjectEntity entityRequest = modelMapper.map(projectRequestDTO, ProjectEntity.class);

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
}
