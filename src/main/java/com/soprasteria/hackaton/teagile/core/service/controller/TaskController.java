package com.soprasteria.hackaton.teagile.core.service.controller;

import java.util.List;

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

import com.soprasteria.hackaton.teagile.core.service.dto.TaskRequestDTO;
import com.soprasteria.hackaton.teagile.core.service.dto.TaskResponseDTO;
import com.soprasteria.hackaton.teagile.core.service.service.TaskService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * Controller for task
 * 
 * @author Igor Dosinchuk
 * @author Luis Rapestre
 * 
 */
@RestController
@Api(value = "API Rest for Task.")
@RequestMapping(value = "/api/v1")
public class TaskController {

	public static final Logger logger = LoggerFactory.getLogger(TaskController.class);

	@Autowired
	TaskService taskService;

	/**
	 * Retrieve list of all tasks.
	 * 
	 * @param pageable paging fields
	 * @return ResponseEntity with paged list of all tasks, headers and status
	 */
	@GetMapping(path = "/tasks", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ApiOperation(value = "Retrieve list of all tasks.")
	public ResponseEntity<List<TaskResponseDTO>> getAllTasksByProjectId(@RequestParam("projectId") int projectId) {

		logger.info("Fetching all tasks by projectId");
		return taskService.getAllTasksByProjectId(projectId);
	}

	/**
	 * Add a task.
	 * 
	 * @param taskRequestDTO object to save
	 * @return ResponseEntity with status and taskResponseDTO
	 */
	@PostMapping(path = "/tasks", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ApiOperation(value = "Add a task.")
	public ResponseEntity<TaskResponseDTO> addTask(@Valid @RequestBody TaskRequestDTO taskRequestDTO) {

		logger.info("Process add task");
		return taskService.addTask(taskRequestDTO);
	}

	/**
	 * Update a task
	 * 
	 * @param id             task id
	 * @param taskRequestDTO object to update
	 * @return ResponseEntity with resource and status
	 */
	@PatchMapping(path = "/tasks/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ApiOperation(value = "Update the task.")
	public ResponseEntity<TaskResponseDTO> updateTask(@PathVariable("id") int id,
			@RequestBody TaskRequestDTO taskRequestDTO) {

		logger.info("Process patch task");
		return taskService.updateTask(id, taskRequestDTO);
	}

	/**
	 * Retrieve task by Id.
	 * 
	 * @param id task Id
	 * @return ResponseEntity with status and taskResponseDTO
	 */
	@DeleteMapping(path = "/tasks/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ApiOperation(value = "Delete task by Id.")
	public ResponseEntity<TaskResponseDTO> deleteTask(@PathVariable("id") int id) {

		logger.info("Deleting task with id {} ", id);
		return taskService.deleteTask(id);
	}
}
