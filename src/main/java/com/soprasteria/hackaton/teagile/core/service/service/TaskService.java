package com.soprasteria.hackaton.teagile.core.service.service;

import org.springframework.http.ResponseEntity;

import com.soprasteria.hackaton.teagile.core.service.dto.TaskRequestDTO;

public interface TaskService {

	ResponseEntity<?> getAllTasksByProjectId(int projectId);

	ResponseEntity<?> addTask(int projectId, TaskRequestDTO taskRequestDTO);

	ResponseEntity<?> updateTask(int taskId, int projectId, TaskRequestDTO taskRequestDTO);

	ResponseEntity<?> deleteTask(int taskId, int projectId);

}
