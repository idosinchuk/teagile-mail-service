package com.soprasteria.hackaton.teagile.core.service.controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.soprasteria.hackaton.teagile.core.service.dto.MeetingRequestDTO;
import com.soprasteria.hackaton.teagile.core.service.dto.MeetingResponseDTO;
import com.soprasteria.hackaton.teagile.core.service.service.MeetingService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * Controller for meeting
 * 
 * @author Igor Dosinchuk
 * @author Luis Rapestre
 * 
 */
@RestController
@Api(value = "API Rest for Meeting.")
@RequestMapping(value = "/api/v1")
public class MeetingController {

	public static final Logger logger = LoggerFactory.getLogger(MeetingController.class);

	@Autowired
	MeetingService meetingService;

	/**
	 * Retrieve list of all meetings.
	 * 
	 * @param pageable paging fields
	 * @return ResponseEntity with paged list of all meetings, headers and status
	 */
	@GetMapping(path = "/meetings", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ApiOperation(value = "Retrieve list of all meetings.")
	public ResponseEntity<List<MeetingResponseDTO>> getAllMeetingsByProjectId(
			@RequestParam("projectId") int projectId) {

		logger.info("Fetching all meetings by projectId");
		return meetingService.getAllMeetingsByProjectId(projectId);
	}

	/**
	 * Add a meeting.
	 * 
	 * @param meetingRequestDTO object to save
	 * @return ResponseEntity with status and meetingResponseDTO
	 */
	@PostMapping(path = "/meetings", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ApiOperation(value = "Add a meeting.")
	public ResponseEntity<MeetingResponseDTO> addMeeting(@Valid @RequestBody MeetingRequestDTO meetingRequestDTO) {

		logger.info("Process add meeting");
		return meetingService.addMeeting(meetingRequestDTO);
	}

	/**
	 * Update a meeting
	 * 
	 * @param id                meeting id
	 * @param meetingRequestDTO object to update
	 * @return ResponseEntity with resource and status
	 */
	@PatchMapping(path = "/meetings/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ApiOperation(value = "Update the meeting.")
	public ResponseEntity<MeetingResponseDTO> updateMeeting(@PathVariable("id") int id,
			@RequestBody MeetingRequestDTO meetingRequestDTO) {

		logger.info("Process patch meeting");
		return meetingService.updateMeeting(id, meetingRequestDTO);
	}

	/**
	 * Retrieve meeting by Id.
	 * 
	 * @param id meeting Id
	 * @return ResponseEntity with status and meetingResponseDTO
	 */
	@DeleteMapping(path = "/meetings/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ApiOperation(value = "Delete meeting by Id.")
	public ResponseEntity<MeetingResponseDTO> deleteMeeting(@PathVariable("id") int id) {

		logger.info("Deleting meeting with id {} ", id);
		return meetingService.deleteMeeting(id);
	}
}
