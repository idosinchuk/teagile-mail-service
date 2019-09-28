package com.soprasteria.hackaton.teagile.core.service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.soprasteria.hackaton.teagile.core.service.entity.ProjectEntity;

/**
 * Repository for project
 * 
 * @author Igor Dosinchuk
 *
 */
public interface ProjectRepository extends JpaRepository<ProjectEntity, Integer> {

	ProjectEntity findById(int id);

	ProjectEntity findByName(String name);

	List<ProjectEntity> findAllByUserId(int userId);

}
