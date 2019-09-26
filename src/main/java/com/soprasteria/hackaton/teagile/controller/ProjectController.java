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

import com.soprasteria.hackaton.teagile.dto.ProjectRequestDTO;
import com.soprasteria.hackaton.teagile.dto.ProjectResponseDTO;
import com.soprasteria.hackaton.teagile.service.ProjectService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * Controller for project
 * 
 * @author Igor Dosinchuk
 * @author Luis Rapestre
 * 
 */
@RestController
@Api(value = "API Rest for insurance projects.")
@RequestMapping(value = "/api/v1")
public class ProjectController {

	public static final Logger logger = LoggerFactory.getLogger(ProjectController.class);

	@Autowired
	ProjectService projectService;

	/**
	 * Retrieve list of all projects according to the search criteria.
	 * 
	 * @param pageable paging fields
	 * @return ResponseEntity with paged list of all projects, headers and status
	 */
	@GetMapping(path = "/projects", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ApiOperation(value = "Retrieve list of all projects.")
	public ResponseEntity<List<ProjectResponseDTO>> getAllProjects() {

		logger.info("Fetching all projects");

		List<ProjectResponseDTO> project = null;

		try {
			project = projectService.getAllProjects();

		} catch (Exception e) {
			logger.error("An error occurred! {}", e.getMessage());
			// TODO: Devolver error
		}

		return new ResponseEntity<>(project, HttpStatus.OK);
	}

	/**
	 * Retrieve project by Id.
	 * 
	 * @param id project id
	 * @return ResponseEntity with status and projectResponseDTO
	 */
	@GetMapping(path = "/projects/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ApiOperation(value = "Retrieve project by the projectCode.")
	public ResponseEntity<?> getProject(@PathVariable("projectCode") int id) {

		logger.info("Fetching project with id {}", id);

		ProjectResponseDTO projectResponseDTO = null;

		try {
			projectResponseDTO = projectService.getProject(id);

			return new ResponseEntity<>(projectResponseDTO, HttpStatus.OK);

		} catch (Exception e) {
			logger.error("An error occurred! {}", e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Add a project.
	 * 
	 * @param projectRequestDTO object to save
	 * @return ResponseEntity with status and projectResponseDTO
	 */
	@PostMapping(path = "/projects", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ApiOperation(value = "Add a project.")
	public ResponseEntity<?> addProject(@Valid @RequestBody ProjectRequestDTO projectRequestDTO) {

		logger.info("Process add project");

		return projectService.addProject(projectRequestDTO);

	}

	/**
	 * Update a project
	 * 
	 * @param id                project id
	 * @param projectRequestDTO object to update
	 * @return ResponseEntity with resource and status
	 */
	@PatchMapping(path = "/projects/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ApiOperation(value = "Update the project.")
	public ResponseEntity<?> updateProject(@PathVariable("id") int id,
			@RequestBody ProjectRequestDTO projectRequestDTO) {

		logger.info("Process patch project");

		return projectService.updateProject(id, projectRequestDTO);

	}
}
