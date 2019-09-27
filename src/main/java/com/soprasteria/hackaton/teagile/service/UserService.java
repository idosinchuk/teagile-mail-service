package com.soprasteria.hackaton.teagile.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.soprasteria.hackaton.teagile.dto.UserRequestDTO;
import com.soprasteria.hackaton.teagile.dto.UserResponseDTO;

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
	List<UserResponseDTO> getAllUsers();

	/**
	 * Find user by id.
	 * 
	 * @param id user id
	 * @return {@link UserResponseDTO}
	 */
	UserResponseDTO getUser(int id);

	/**
	 * Find user by login passing the loginName and loginPassword.
	 * 
	 * @param loginName     user login name
	 * @param loginPassword user login password
	 * @return {@link UserResponseDTO}
	 */
	UserResponseDTO getUserByLogin(String loginName, String loginPassword);

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
}
