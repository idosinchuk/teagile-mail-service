package com.soprasteria.hackaton.teagile.core.service.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.soprasteria.hackaton.teagile.core.service.dto.TaskRequestDTO;
import com.soprasteria.hackaton.teagile.core.service.dto.TaskResponseDTO;

public interface TaskService {

	/**
	 * Retrieve list of all tasks.
	 * 
	 * @param projectId project id
	 * @return List of {@link TaskResponseDTO}
	 */
	ResponseEntity<List<TaskResponseDTO>> getAllTasksByProjectId(int projectId);

	/**
	 * Find task by id.
	 * 
	 * @param id task id
	 * @return {@link TaskResponseDTO}
	 */
	ResponseEntity<TaskResponseDTO> getTask(int id);

	/**
	 * Add a task.
	 * 
	 * @param taskRequestDTO object to save
	 * 
	 * @return ResponseEntity
	 */
	ResponseEntity<TaskResponseDTO> addTask(TaskRequestDTO taskRequestDTO);

	/**
	 * Update the Task.
	 * 
	 * @param id             task id
	 * @param taskRequestDTO object to save
	 * 
	 * @return {@link TaskResponseDTO}
	 */
	ResponseEntity<TaskResponseDTO> updateTask(int id, TaskRequestDTO taskRequestDTO);

	/**
	 * Delete task by id.
	 * 
	 * @param id task id
	 * @return {@link TaskResponseDTO}
	 */
	ResponseEntity<TaskResponseDTO> deleteTask(int id);

}