package com.soprasteria.hackaton.teagile.core.service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.soprasteria.hackaton.teagile.core.service.entity.MeetingEntity;

public interface MeetingRepository extends JpaRepository<MeetingEntity, Integer> {

	MeetingEntity findByIdAndProjectId(int id, int projectId);

	void deleteByIdAndProjectId(int id, int projectId);
	
	List<MeetingEntity> findByProject_Id(int id);

}
