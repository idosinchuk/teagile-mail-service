package com.soprasteria.hackaton.teagile.core.service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.soprasteria.hackaton.teagile.core.service.entity.TaskEntity;
import com.soprasteria.hackaton.teagile.core.service.entity.UserEntity;

/**
 * Repository for task
 * 
 * @author Igor Dosinchuk
 *
 */
public interface TaskRepository extends JpaRepository<TaskEntity, Integer> {

	TaskEntity findById (int id);
	
	List<TaskEntity> findAllByProjectId(int projectId);
}
