package com.soprasteria.hackaton.teagile.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.soprasteria.hackaton.teagile.entity.UserEntity;

/**
 * Repository for user
 * 
 * @author Igor Dosinchuk
 *
 */
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

	UserEntity findById(int id);

	UserEntity findByLoginName(String loginName);

	@Query("SELECT u FROM User u where u.login_name = ?1 AND u.login_password = ?2")
	UserEntity findByLoginNameAndLoginPassword(String loginName, String loginPassword);

}
