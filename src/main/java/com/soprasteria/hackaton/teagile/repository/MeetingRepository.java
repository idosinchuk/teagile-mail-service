package com.soprasteria.hackaton.teagile.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.soprasteria.hackaton.teagile.entity.MeetingEntity;

/**
 * Repository for task
 * 
 * @author Igor Dosinchuk
 *
 */
public interface MeetingRepository extends JpaRepository<MeetingEntity, Integer> {

	MeetingEntity findById(String id);

}
