package com.soprasteria.hackaton.teagile.core.service.service;

import org.springframework.http.ResponseEntity;

import com.soprasteria.hackaton.teagile.core.service.dto.MeetingRequestDTO;

public interface MeetingService {

	ResponseEntity<?> getAllMeetingsByProjectId(int projectId);

	ResponseEntity<?> addMeeting(int projectId, MeetingRequestDTO meetingRequestDTO);

	ResponseEntity<?> updateMeeting(int meetingId, int projectId, MeetingRequestDTO meetingRequestDTO);

	ResponseEntity<?> deleteMeeting(int meetingId, int projectId);

}
