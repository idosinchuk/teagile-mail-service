package com.soprasteria.hackaton.teagile.service;

import java.util.List;

import com.soprasteria.hackaton.teagile.dto.ProjectResponseDTO;

/**
 * 
 * Service for project
 * 
 * @author Igor Dosinchuk
 *
 */
public interface ProjectService {

	/**
	 * Retrieve list of all projects according to the search criteria.
	 * 
	 * @param pageable object for pagination
	 * @return Page of {@link ProjectResponseDTO}
	 */
	List<ProjectResponseDTO> getAllProjects();

	/**
	 * Find projects by the projectCode.
	 * 
	 * @param projectCode project code
	 * @return {@link ProjectResponseDTO}
	 */
	ProjectResponseDTO getProjects(String projectCode);

	/**
	 * Add a project..
	 * 
	 * @param projectRequestDTO object to save
	 * 
	 * @return ProjectResponseDTO
	 */
	ProjectResponseDTO addProject(ProjectRequestDTO projectRequestDTO);

	/**
	 * Update the project.
	 * 
	 * @param projectCode       project code
	 * @param projectRequestDTO object to save
	 * 
	 * @return ProjectResponseDTO
	 */
	ProjectResponseDTO updateProject(String projectCode, ProjectRequestDTO projectRequestDTO);
}
