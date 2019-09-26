package com.soprasteria.hackaton.teagile.service;

import java.util.List;

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
	List<UserResponseDTO> getUsers();

	/**
	 * Find user by the loginName.
	 * 
	 * @param userCode user code
	 * @return {@link UserResponseDTO}
	 */
	UserResponseDTO getUser(String loginName);

	/**
	 * Add a user..
	 * 
	 * @param userRequestDTO object to save
	 * 
	 * @return UserResponseDTO
	 */
	UserResponseDTO addUser(UserRequestDTO userRequestDTO);

	/**
	 * Update the user.
	 * 
	 * @param userCode       user code
	 * @param userRequestDTO object to save
	 * 
	 * @return UserResponseDTO
	 */
	UserResponseDTO updateUser(String userCode, UserRequestDTO userRequestDTO);
}
