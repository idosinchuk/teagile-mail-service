package com.soprasteria.hackaton.teagile.core.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.soprasteria.hackaton.teagile.core.service.entity.MeetingEntity;

/**
 * Repository for task
 * 
 * @author Igor Dosinchuk
 *
 */
public interface MeetingRepository extends JpaRepository<MeetingEntity, Integer> {

	MeetingEntity findById(String id);

}
