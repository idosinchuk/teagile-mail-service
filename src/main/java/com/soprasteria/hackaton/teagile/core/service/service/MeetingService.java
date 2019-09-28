package com.soprasteria.hackaton.teagile.core.service.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.soprasteria.hackaton.teagile.core.service.dto.MeetingRequestDTO;
import com.soprasteria.hackaton.teagile.core.service.dto.MeetingResponseDTO;

public interface MeetingService {

	/**
	 * Retrieve list of all meetings.
	 * 
	 * @param projectId project id
	 * @return List of {@link MeetingResponseDTO}
	 */
	ResponseEntity<List<MeetingResponseDTO>> getAllMeetingsByProjectId(int projectId);

	/**
	 * Find meeting by id.
	 * 
	 * @param id meeting id
	 * @return {@link MeetingResponseDTO}
	 */
	ResponseEntity<MeetingResponseDTO> getMeeting(int id);

	/**
	 * Add a meeting.
	 * 
	 * @param meetingRequestDTO object to save
	 * 
	 * @return ResponseEntity
	 */
	ResponseEntity<MeetingResponseDTO> addMeeting(MeetingRequestDTO meetingRequestDTO);

	/**
	 * Update the Meeting.
	 * 
	 * @param id                meeting id
	 * @param meetingRequestDTO object to save
	 * 
	 * @return {@link MeetingResponseDTO}
	 */
	ResponseEntity<MeetingResponseDTO> updateMeeting(int id, MeetingRequestDTO meetingRequestDTO);

	/**
	 * Delete meeting by id.
	 * 
	 * @param id meeting id
	 * @return {@link MeetingResponseDTO}
	 */
	ResponseEntity<MeetingResponseDTO> deleteMeeting(int id);

}
