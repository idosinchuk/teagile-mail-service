package com.soprasteria.hackaton.teagile.core.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.soprasteria.hackaton.teagile.core.service.entity.UserEntity;

/**
 * Repository for user
 * 
 * @author Igor Dosinchuk
 *
 */
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

	UserEntity findById(int id);

	UserEntity findByEmail(String email);

	// @Query("SELECT u FROM User u where u.email = ?1 AND u.password = ?2")
	UserEntity findByEmailAndPassword(String email, String password);

}
