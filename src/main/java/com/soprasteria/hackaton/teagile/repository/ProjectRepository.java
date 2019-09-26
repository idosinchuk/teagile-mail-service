package com.soprasteria.hackaton.teagile.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.soprasteria.hackaton.teagile.entity.ProjectEntity;

/**
 * Repository for project
 * 
 * @author Igor Dosinchuk
 *
 */
public interface ProjectRepository extends JpaRepository<ProjectEntity, Integer> {

	ProjectEntity findById(int id);

	ProjectEntity findByName(String name);

}
