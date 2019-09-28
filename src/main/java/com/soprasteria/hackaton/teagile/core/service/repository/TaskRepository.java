package com.soprasteria.hackaton.teagile.core.service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.soprasteria.hackaton.teagile.core.service.entity.TaskEntity;

/**
 * Repository for task
 * 
 * @author Igor Dosinchuk
 *
 */
public interface TaskRepository extends JpaRepository<TaskEntity, Integer> {

	TaskEntity findByIdAndProjectId(int id, int projectId);

	void deleteByIdAndProjectId(int id, int projectId);

	List<TaskEntity> findAllByProjectId(int projectId);
}
