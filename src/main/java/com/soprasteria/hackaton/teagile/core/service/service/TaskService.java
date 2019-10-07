package com.soprasteria.hackaton.teagile.core.service.service;

import org.springframework.http.ResponseEntity;

import com.soprasteria.hackaton.teagile.core.service.dto.TaskRequestDTO;

public interface TaskService {

	ResponseEntity<?> getAllTasksByProjectId(int projectId);

	ResponseEntity<?> addMeetingByProjectId(int projectId, TaskRequestDTO taskRequestDTO);

	ResponseEntity<?> updateTaskByProjectIdAndMeetingId(int taskId, int projectId, TaskRequestDTO taskRequestDTO);

	ResponseEntity<?> deleteTaskByProjectIdAndMeetingId(int taskId, int projectId);

}
