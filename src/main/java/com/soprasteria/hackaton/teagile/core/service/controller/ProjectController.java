package com.soprasteria.hackaton.teagile.core.service.controller;

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

import com.soprasteria.hackaton.teagile.core.service.dto.ProjectRequestDTO;
import com.soprasteria.hackaton.teagile.core.service.service.ProjectService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "API Rest for Project.")
@RequestMapping(value = "/api/v1")
public class ProjectController {

	public static final Logger logger = LoggerFactory.getLogger(ProjectController.class);

	@Autowired
	ProjectService projectService;

	@GetMapping(path = "/users/{userId}/projects", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ApiOperation(value = "Retrieve list of all projects by userId")
	public ResponseEntity<?> getAllProjectsByUserId(@PathVariable("userId") int userId) {

		logger.info("Fetching all projects by UserId");
		return projectService.getAllProjectsByUserId(userId);
	}
	


	@GetMapping(path = "projects/{projectId}/users/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ApiOperation(value = "Retrieve project by projectId and userId.")
	public ResponseEntity<?> getProject(@PathVariable("projectId") int projectId, @PathVariable("userId") int userId) {

		logger.info("Fetching project with id and userId {}", projectId, userId);
		return projectService.getProjectByIdUserId(projectId, userId);
	}

	@PostMapping(path = "/projects", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ApiOperation(value = "Add a project.")
	public ResponseEntity<?> addProject(@Valid @RequestBody ProjectRequestDTO projectRequestDTO,
			@RequestParam("userId") int userId) {

		logger.info("Process add project");

		return projectService.addProject(projectRequestDTO, userId);

	}
	
	@PostMapping(path = "/projects/{projectId}/users/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Add project to user")
	public ResponseEntity<?> addUserToProject(@PathVariable("projectId") int projectId, @PathVariable("userId") int userId) {

		logger.info("Add project to user with projectId and userId {}", projectId, userId);
		return projectService.addUserToProject(projectId, userId);
	}

	@PatchMapping(path = "/projects/{projectId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ApiOperation(value = "Update the project.")
	public ResponseEntity<?> updateProject(@PathVariable("projectId") int projectId,
			@RequestBody ProjectRequestDTO projectRequestDTO) {

		logger.info("Process patch project");

		return projectService.updateProject(projectId, projectRequestDTO);

	}

	@DeleteMapping(path = "/projects/{projectId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ApiOperation(value = "Delete project by projectId.")
	public ResponseEntity<?> deleteUser(@PathVariable("projectId") int projectId) {

		logger.info("Deleting user with projectId {} ", projectId);
		return projectService.deleteProject(projectId);
	}
}
