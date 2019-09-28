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
	 * @param id                project id
	 * 
	 * @param meetingRequestDTO object to save
	 * 
	 * @return {@link MeetingResponseDTO}
	 */
	ResponseEntity<MeetingResponseDTO> updateMeeting(int id, int projectId, MeetingRequestDTO meetingRequestDTO);

	/**
	 * Delete meeting by id.
	 * 
	 * @param id meeting id
	 * @param id project id
	 * 
	 * @return {@link MeetingResponseDTO}
	 */
	ResponseEntity<MeetingResponseDTO> deleteMeeting(int id, int projectId);

}
