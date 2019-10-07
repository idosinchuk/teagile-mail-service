package com.soprasteria.hackaton.teagile.core.service.service;

import org.springframework.http.ResponseEntity;

import com.soprasteria.hackaton.teagile.core.service.dto.MeetingRequestDTO;

public interface MeetingService {

	ResponseEntity<?> getAllMeetingsByProjectId(int projectId);

	ResponseEntity<?> addMeetingByProjectId(int projectId, MeetingRequestDTO meetingRequestDTO);

	ResponseEntity<?> updateMeetingByProjectId(int meetingId, int projectId, MeetingRequestDTO meetingRequestDTO);

	ResponseEntity<?> deleteMeetingByProjectId(int meetingId, int projectId);

}
