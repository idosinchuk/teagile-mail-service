package com.soprasteria.hackaton.teagile.core.service.service.impl;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soprasteria.hackaton.teagile.core.service.common.ArrayListCustomMessage;
import com.soprasteria.hackaton.teagile.core.service.common.CustomErrorType;
import com.soprasteria.hackaton.teagile.core.service.common.CustomMessage;
import com.soprasteria.hackaton.teagile.core.service.controller.MeetingController;
import com.soprasteria.hackaton.teagile.core.service.controller.UserController;
import com.soprasteria.hackaton.teagile.core.service.dto.MeetingRequestDTO;
import com.soprasteria.hackaton.teagile.core.service.dto.MeetingResponseDTO;
import com.soprasteria.hackaton.teagile.core.service.entity.MeetingEntity;
import com.soprasteria.hackaton.teagile.core.service.entity.ProjectEntity;
import com.soprasteria.hackaton.teagile.core.service.mail.MailClient;
import com.soprasteria.hackaton.teagile.core.service.repository.MeetingRepository;
import com.soprasteria.hackaton.teagile.core.service.repository.ProjectRepository;
import com.soprasteria.hackaton.teagile.core.service.service.MeetingService;

@Service("Meetingservice")
public class MeetingServiceImpl implements MeetingService {

	@Autowired
	private MeetingRepository meetingRepository;

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	MailClient mailClient;

	public static final Logger logger = LoggerFactory.getLogger(MeetingServiceImpl.class);

	public ResponseEntity<?> getAllMeetingsByProjectId(int projectId) {

		Resources<CustomMessage> resource = null;
		List<CustomMessage> customMessageList = null;
		List<MeetingResponseDTO> meetings = new ArrayList<>();

		try {

			List<MeetingEntity> entityResponse = meetingRepository.findByProject_Id(projectId);

			if (entityResponse == null) {
				customMessageList = ArrayListCustomMessage.setMessage("There are not meetings!", HttpStatus.NO_CONTENT);
				resource = new Resources<>(customMessageList);
				resource.add(linkTo(MeetingController.class).withSelfRel());
				return new ResponseEntity<>(resource, HttpStatus.NO_CONTENT);
			}

			// Convert Entity response to DTO
			meetings = modelMapper.map(entityResponse, new TypeToken<List<MeetingResponseDTO>>() {
			}.getType());

		} catch (Exception e) {
			logger.error("An error occurred! {}", e.getMessage());
			return CustomErrorType.returnResponsEntityError(e.getMessage());
		}

		return new ResponseEntity<>(meetings, HttpStatus.OK);

	}
	
	@Transactional
	public ResponseEntity<?> addMeetingByProjectId(int projectId, MeetingRequestDTO meetingRequestDTO) {

		Resources<CustomMessage> resource = null;
		List<CustomMessage> customMessageList = null;
		MeetingResponseDTO meetingResponseDTO = null;

		try {
			ProjectEntity projectEntity = projectRepository.findById(projectId);

			// Check if projectId exists in the database
			if (projectEntity == null) {
				customMessageList = ArrayListCustomMessage.setMessage(
						"The projectId does not exists. Please try with valid projectId.", HttpStatus.NO_CONTENT);
				resource = new Resources<>(customMessageList);
				resource.add(linkTo(UserController.class).withSelfRel());
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			checkPriorityAndStatus(meetingRequestDTO);

			// Convert meetingRequestDTO to MeetingEntity
			MeetingEntity entityRequest = modelMapper.map(meetingRequestDTO, MeetingEntity.class);

			MeetingEntity meetingEntityResponse = meetingRepository.save(entityRequest);

			// Convert meetingEntityResponse to MeetingResponseDTO
			meetingResponseDTO = modelMapper.map(meetingEntityResponse, MeetingResponseDTO.class);

		} catch (Exception e) {
			logger.error("An error occurred! {}", e.getMessage());
			return CustomErrorType.returnResponsEntityError(e.getMessage());
		}

		return new ResponseEntity<>(meetingResponseDTO, HttpStatus.CREATED);

	}

	@Transactional
	public ResponseEntity<?> updateMeetingByProjectIdAndMeetingId(int meetingId, int projectId, MeetingRequestDTO meetingRequestDTO) {

		List<CustomMessage> customMessageList = null;
		Resources<CustomMessage> resource = null;

		try {
			// Check if meeting exists in the database
			MeetingEntity meetingEntity = meetingRepository.findByIdAndProjectId(meetingId, projectId);

			if (meetingEntity == null) {
				customMessageList = ArrayListCustomMessage.setMessage("Meeting Id {}" + meetingId + " Not Found!",
						HttpStatus.NO_CONTENT);
				resource = new Resources<>(customMessageList);
				resource.add(linkTo(MeetingController.class).withSelfRel());
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			MeetingEntity entityRequest = modelMapper.map(meetingRequestDTO, MeetingEntity.class);
			entityRequest.setId(meetingId);
			
			meetingRepository.save(entityRequest);

		} catch (Exception e) {
			logger.error("An error occurred! {}", e.getMessage());
			return CustomErrorType.returnResponsEntityError(e.getMessage());

		}

		return new ResponseEntity<>(HttpStatus.OK);

	}

	public ResponseEntity<?> deleteMeetingByProjectIdAndMeetingId(int meetingId, int projectId) {

		try {
			meetingRepository.deleteByIdAndProjectId(meetingId, projectId);

		} catch (Exception e) {
			logger.error("An error occurred! {}", e.getMessage());
			return CustomErrorType.returnResponsEntityError(e.getMessage());
		}

		return new ResponseEntity<>(HttpStatus.OK);

	}

	// Check if priority and status are present
	private static MeetingRequestDTO checkPriorityAndStatus(MeetingRequestDTO meetingRequestDTO) {

		// If priority is null, set default priority.
		if (meetingRequestDTO.getPriority() == 0) {
			meetingRequestDTO.setPriority(5);
		}

		// If status is null, set default CREATED status.
		if (meetingRequestDTO.getStatus() == null) {
			meetingRequestDTO.setStatus("CREATED");
		}

		return meetingRequestDTO;
	}

}
