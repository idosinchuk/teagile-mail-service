package com.soprasteria.hackaton.teagile.core.service.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.soprasteria.hackaton.teagile.core.service.entity.ProjectEntity;

public interface ProjectRepository extends JpaRepository<ProjectEntity, Integer> {

	ProjectEntity findById(int id);

	ProjectEntity findByName(String name);
	
	ProjectEntity findById_AndUsers_Id(int id, int userId);

	Set<ProjectEntity> findByUsers_Id(int userId);

}
