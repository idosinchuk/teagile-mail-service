package com.soprasteria.hackaton.teagile.core.service.service;

import org.springframework.http.ResponseEntity;

import com.soprasteria.hackaton.teagile.core.service.dto.ProjectRequestDTO;

public interface ProjectService {

	ResponseEntity<?> getAllProjectsByUserId(int userId);

	ResponseEntity<?> getProject(int id);

	ResponseEntity<?> addProject(ProjectRequestDTO projectRequestDTO, int userId);

	ResponseEntity<?> updateProject(int id, ProjectRequestDTO projectRequestDTO);

	ResponseEntity<?> deleteProject(int id);
}
