package com.soprasteria.hackaton.teagile.core.service.service.impl;

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

import com.soprasteria.hackaton.teagile.core.service.common.ArrayListCustomMessage;
import com.soprasteria.hackaton.teagile.core.service.common.CustomErrorType;
import com.soprasteria.hackaton.teagile.core.service.common.CustomMessage;
import com.soprasteria.hackaton.teagile.core.service.controller.TaskController;
import com.soprasteria.hackaton.teagile.core.service.controller.UserController;
import com.soprasteria.hackaton.teagile.core.service.dto.TaskRequestDTO;
import com.soprasteria.hackaton.teagile.core.service.dto.TaskResponseDTO;
import com.soprasteria.hackaton.teagile.core.service.entity.ProjectEntity;
import com.soprasteria.hackaton.teagile.core.service.entity.TaskEntity;
import com.soprasteria.hackaton.teagile.core.service.mail.MailClient;
import com.soprasteria.hackaton.teagile.core.service.repository.ProjectRepository;
import com.soprasteria.hackaton.teagile.core.service.repository.TaskRepository;
import com.soprasteria.hackaton.teagile.core.service.service.TaskService;

/**
 * Implementation for Task service
 * 
 * @author Igor Dosinchuk
 *
 */
@Service("Taskservice")
public class TaskServiceImpl implements TaskService {

	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	MailClient mailClient;

	public static final Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);

	/**
	 * {@inheritDoc}
	 */
	public ResponseEntity<List<TaskResponseDTO>> getAllTasksByProjectId(int projectId) {

		List<TaskEntity> entityResponse = taskRepository.findAllByProjectId(projectId);

		if (entityResponse == null) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}

		// Convert Entity response to DTO
		List<TaskResponseDTO> tasks = modelMapper.map(entityResponse, new TypeToken<List<TaskResponseDTO>>() {
		}.getType());

		return new ResponseEntity<>(tasks, HttpStatus.OK);

	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public ResponseEntity<TaskResponseDTO> getTask(int id) {

		TaskResponseDTO TaskResponseDTO = null;
		Resources<CustomMessage> resource = null;

		try {
			List<CustomMessage> customMessageList = null;

			TaskEntity entityResponse = taskRepository.findById(id);

			if (entityResponse == null) {
				customMessageList = ArrayListCustomMessage
						.setMessage("The requested Task does not exists. Please try again.", HttpStatus.NO_CONTENT);
				resource = new Resources<>(customMessageList);
				resource.add(linkTo(TaskController.class).withSelfRel());

				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			TaskResponseDTO = modelMapper.map(entityResponse, TaskResponseDTO.class);

		} catch (Exception e) {
			logger.error("An error occurred! {}", e.getMessage());
			return CustomErrorType.returnResponsEntityError(e.getMessage());
		}

		return new ResponseEntity<>(TaskResponseDTO, HttpStatus.OK);

	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public ResponseEntity<TaskResponseDTO> addTask(TaskRequestDTO taskRequestDTO) {

		Resources<CustomMessage> resource = null;
		TaskResponseDTO taskResponseDTO = null;

		try {
			List<CustomMessage> customMessageList = null;

			ProjectEntity projectEntity = projectRepository.findById(taskRequestDTO.getProjectId());

			// Check if projectId exists in the database
			if (projectEntity == null) {
				customMessageList = ArrayListCustomMessage.setMessage(
						"The projectId does not exists. Please try with valid projectId.", HttpStatus.BAD_REQUEST);
				resource = new Resources<>(customMessageList);
				resource.add(linkTo(UserController.class).withSelfRel());

				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}

			// If priority is null, set default priority.
			if (taskRequestDTO.getPriority() == 0) {
				taskRequestDTO.setPriority(5);
			}

			// If status is null, set default status.
			if (taskRequestDTO.getStatus() == null) {
				taskRequestDTO.setStatus("created");
			}

			// Convert taskRequestDTO to TaskEntity
			TaskEntity entityRequest = modelMapper.map(taskRequestDTO, TaskEntity.class);

			TaskEntity taskEntityResponse = taskRepository.save(entityRequest);

			// Convert taskEntityResponse to TaskResponseDTO
			taskResponseDTO = modelMapper.map(taskEntityResponse, TaskResponseDTO.class);

			customMessageList = ArrayListCustomMessage.setMessage("Created new Task", HttpStatus.CREATED);

			resource = new Resources<>(customMessageList);
			resource.add(linkTo(TaskController.class).withSelfRel());
		} catch (Exception e) {
			logger.error("An error occurred! {}", e.getMessage());
			return CustomErrorType.returnResponsEntityError(e.getMessage());
		}

		return new ResponseEntity<>(taskResponseDTO, HttpStatus.OK);

	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional
	public ResponseEntity<TaskResponseDTO> updateTask(int id, TaskRequestDTO taskRequestDTO) {

		Resources<CustomMessage> resource = null;
		TaskResponseDTO taskResponseDTO = null;

		try {

			List<CustomMessage> customMessageList = null;

			customMessageList = ArrayListCustomMessage.setMessage("Patch task process", HttpStatus.OK);

			// Check if request is null
			if (taskRequestDTO == null) {
				customMessageList = ArrayListCustomMessage.setMessage("Request body is null!", HttpStatus.BAD_REQUEST);
				resource = new Resources<>(customMessageList);
				resource.add(linkTo(TaskController.class).withSelfRel());
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}

			// Find Task by ID for check if exists in DB
			TaskEntity taskEntity = taskRepository.findById(id);

			// If exists
			if (taskEntity != null) {

				// The Task ID will always be the same, so we do not allow it to
				// be updated, for them we overwrite the field with the original value.
				taskRequestDTO.setId(taskEntity.getId());

				TaskEntity entityRequest = modelMapper.map(taskRequestDTO, TaskEntity.class);
				taskRepository.save(entityRequest);

			} else {
				customMessageList = ArrayListCustomMessage.setMessage("Task id " + id + " Not Found!",
						HttpStatus.BAD_REQUEST);
				resource = new Resources<>(customMessageList);
				resource.add(linkTo(TaskController.class).withSelfRel());
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}

			resource = new Resources<>(customMessageList);
			resource.add(linkTo(TaskController.class).slash(taskRequestDTO.getId()).withSelfRel());
		} catch (Exception e) {
			logger.error("An error occurred! {}", e.getMessage());
			return CustomErrorType.returnResponsEntityError(e.getMessage());

		}

		return new ResponseEntity<>(taskResponseDTO, HttpStatus.OK);

	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public ResponseEntity<TaskResponseDTO> deleteTask(int id) {

		TaskResponseDTO taskResponseDTO = null;

		try {
			taskRepository.deleteById(id);

		} catch (Exception e) {
			logger.error("An error occurred! {}", e.getMessage());
			return CustomErrorType.returnResponsEntityError(e.getMessage());
		}

		return new ResponseEntity<>(taskResponseDTO, HttpStatus.OK);

	}
}
