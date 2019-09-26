package com.soprasteria.hackaton.teagile.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.soprasteria.hackaton.teagile.entity.TaskEntity;

/**
 * Repository for task
 * 
 * @author Igor Dosinchuk
 *
 */
public interface TaskRepository extends JpaRepository<TaskEntity, Integer> {

	TaskEntity findById(String id);
}
