package com.soprasteria.hackaton.teagile.core.service.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.soprasteria.hackaton.teagile.core.service.dto.ProjectResponseDTO;
import com.soprasteria.hackaton.teagile.core.service.dto.UserRequestDTO;
import com.soprasteria.hackaton.teagile.core.service.dto.UserResponseDTO;

/**
 * 
 * Service for user
 * 
 * @author Igor Dosinchuk
 *
 */
public interface UserService {

	/**
	 * Retrieve list of all users.
	 * 
	 * @return List of {@link UserResponseDTO}
	 */
	ResponseEntity<List<UserResponseDTO>> getAllUsers();

	/**
	 * Find user by id.
	 * 
	 * @param id user id
	 * @return {@link UserResponseDTO}
	 */
	ResponseEntity<UserResponseDTO> getUser(int id);

	/**
	 * Find user by email.
	 * 
	 * @param email user email
	 * @return {@link UserResponseDTO}
	 */
	ResponseEntity<UserResponseDTO> getUserByEmail(String email);

	/**
	 * Find user by email and password.
	 * 
	 * @param email    user email
	 * @param password user password
	 * @return {@link UserResponseDTO}
	 */
	ResponseEntity<UserResponseDTO> getUserByEmailAndPassword(String email, String password);

	/**
	 * Add project to user with userRequestDTO and projectId.
	 * 
	 * @param userRequestDTO object to save
	 * @param projectId      project id
	 * @return {@link ProjectResponseDTO}
	 */
	ResponseEntity<?> addUserToProject(UserRequestDTO userRequestDTO, int projectId);

	/**
	 * Add a user.
	 * 
	 * @param userRequestDTO object to save
	 * 
	 * @return ResponseEntity
	 */
	ResponseEntity<?> addUser(UserRequestDTO userRequestDTO);

	/**
	 * Update the user.
	 * 
	 * @param id             user id
	 * @param userRequestDTO object to save
	 * 
	 * @return ResponseEntity
	 */
	ResponseEntity<?> updateUser(int id, UserRequestDTO userRequestDTO);

	/**
	 * Delete user by id.
	 * 
	 * @param id user id
	 * @return {@link UserResponseDTO}
	 */
	ResponseEntity<UserResponseDTO> deleteUser(int id);
}
