package com.soprasteria.hackaton.teagile.core.service.service;

import org.springframework.http.ResponseEntity;

import com.soprasteria.hackaton.teagile.core.service.dto.UserRequestDTO;

public interface UserService {

	ResponseEntity<?> getAllUsers();

	ResponseEntity<?> getUser(int userId);

	ResponseEntity<?> getUserByEmail(String email);

	ResponseEntity<?> getUserByEmailAndPassword(String email, String password);

	ResponseEntity<?> addUserToProject(int projectId, int userId);

	ResponseEntity<?> addUser(UserRequestDTO userRequestDTO);

	ResponseEntity<?> updateUser(int id, UserRequestDTO userRequestDTO);

	ResponseEntity<?> deleteUser(int userId);
}
