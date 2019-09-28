package com.soprasteria.hackaton.teagile.core.service.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.soprasteria.hackaton.teagile.core.service.dto.ProjectRequestDTO;
import com.soprasteria.hackaton.teagile.core.service.dto.ProjectResponseDTO;

/**
 * 
 * Service for project
 * 
 * @author Igor Dosinchuk
 *
 */
public interface ProjectService {

	/**
	 * Retrieve list of all projects filtered by userId
	 * 
	 * @param userId user id
	 * @return Page of {@link ProjectResponseDTO}
	 */
	ResponseEntity<List<ProjectResponseDTO>> getAllProjectsByUserId(int userId);

	/**
	 * Find project by id.
	 * 
	 * @param id project id
	 * @return {@link ProjectResponseDTO}
	 */
	ResponseEntity<ProjectResponseDTO> getProject(int id);

	/**
	 * Add a project.
	 * 
	 * @param projectRequestDTO object to save
	 * @param id                user id
	 * 
	 * @return ResponseEntity
	 */
	ResponseEntity<?> addProject(ProjectRequestDTO projectRequestDTO, int userId);

	/**
	 * Update the project.
	 * 
	 * @param id                project id
	 * @param projectRequestDTO object to save
	 * 
	 * @return ResponseEntity
	 */
	ResponseEntity<?> updateProject(int id, ProjectRequestDTO projectRequestDTO);

	/**
	 * Delete project by id.
	 * 
	 * @param id project id
	 * @return {@link ProjectResponseDTO}
	 */
	ResponseEntity<ProjectResponseDTO> deleteProject(int id);
}
