package com.soprasteria.hackaton.teagile.core.service.service.impl;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

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

/**
 * Implementation for Meeting service
 * 
 * @author Igor Dosinchuk
 *
 */
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

	/**
	 * {@inheritDoc}
	 */
	public ResponseEntity<List<MeetingResponseDTO>> getAllMeetingsByProjectId(int projectId) {

		List<MeetingEntity> entityResponse = null;
		// List<MeetingEntity> entityResponse =
		// meetingRepository.findAllByProjectId(projectId);

		if (entityResponse == null) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}

		// Convert Entity response to DTO
		List<MeetingResponseDTO> meetings = modelMapper.map(entityResponse, new TypeToken<List<MeetingResponseDTO>>() {
		}.getType());

		return new ResponseEntity<>(meetings, HttpStatus.OK);

	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public ResponseEntity<MeetingResponseDTO> addMeeting(MeetingRequestDTO meetingRequestDTO) {

		Resources<CustomMessage> resource = null;
		MeetingResponseDTO meetingResponseDTO = null;

		try {
			List<CustomMessage> customMessageList = null;

			ProjectEntity projectEntity = projectRepository.findById(meetingRequestDTO.getProjectId());

			// Check if projectId exists in the database
			if (projectEntity == null) {
				customMessageList = ArrayListCustomMessage.setMessage(
						"The projectId does not exists. Please try with valid projectId.", HttpStatus.BAD_REQUEST);
				resource = new Resources<>(customMessageList);
				resource.add(linkTo(UserController.class).withSelfRel());

				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}

			// If priority is null, set default priority.
			if (meetingRequestDTO.getPriority() == 0) {
				meetingRequestDTO.setPriority(5);
			}

			// If status is null, set default status.
			if (meetingRequestDTO.getStatus() == null) {
				meetingRequestDTO.setStatus("created");
			}

			// Convert meetingRequestDTO to MeetingEntity
			MeetingEntity entityRequest = modelMapper.map(meetingRequestDTO, MeetingEntity.class);

			MeetingEntity meetingEntityResponse = meetingRepository.save(entityRequest);

			// Convert meetingEntityResponse to MeetingResponseDTO
			meetingResponseDTO = modelMapper.map(meetingEntityResponse, MeetingResponseDTO.class);

			customMessageList = ArrayListCustomMessage.setMessage("Created new Meeting", HttpStatus.CREATED);

			resource = new Resources<>(customMessageList);
			resource.add(linkTo(MeetingController.class).withSelfRel());
		} catch (Exception e) {
			logger.error("An error occurred! {}", e.getMessage());
			return CustomErrorType.returnResponsEntityError(e.getMessage());
		}

		return new ResponseEntity<>(meetingResponseDTO, HttpStatus.OK);

	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional
	public ResponseEntity<MeetingResponseDTO> updateMeeting(int id, int projectId,
			MeetingRequestDTO meetingRequestDTO) {

		Resources<CustomMessage> resource = null;
		MeetingResponseDTO meetingResponseDTO = null;

		try {

			List<CustomMessage> customMessageList = null;

			customMessageList = ArrayListCustomMessage.setMessage("Patch meeting process", HttpStatus.OK);

			// Check if request is null
			if (meetingRequestDTO == null) {
				customMessageList = ArrayListCustomMessage.setMessage("Request body is null!", HttpStatus.BAD_REQUEST);
				resource = new Resources<>(customMessageList);
				resource.add(linkTo(MeetingController.class).withSelfRel());
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}

			// Find Meeting by ID for check if exists in DB
			MeetingEntity meetingEntity = meetingRepository.findByIdAndProjectId(id, projectId);

			// If exists
			if (meetingEntity != null) {

				// The Meeting ID will always be the same, so we do not allow it to
				// be updated, for them we overwrite the field with the original value.
				meetingRequestDTO.setId(meetingEntity.getId());

				MeetingEntity entityRequest = modelMapper.map(meetingRequestDTO, MeetingEntity.class);
				meetingRepository.save(entityRequest);

			} else {
				customMessageList = ArrayListCustomMessage.setMessage("Meeting id " + id + " Not Found!",
						HttpStatus.BAD_REQUEST);
				resource = new Resources<>(customMessageList);
				resource.add(linkTo(MeetingController.class).withSelfRel());
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}

			resource = new Resources<>(customMessageList);
			resource.add(linkTo(MeetingController.class).slash(meetingRequestDTO.getId()).withSelfRel());
		} catch (Exception e) {
			logger.error("An error occurred! {}", e.getMessage());
			return CustomErrorType.returnResponsEntityError(e.getMessage());

		}

		return new ResponseEntity<>(meetingResponseDTO, HttpStatus.OK);

	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public ResponseEntity<MeetingResponseDTO> deleteMeeting(int id, int projectId) {

		MeetingResponseDTO meetingResponseDTO = null;

		try {
			meetingRepository.deleteByIdAndProjectId(id, projectId);

		} catch (Exception e) {
			logger.error("An error occurred! {}", e.getMessage());
			return CustomErrorType.returnResponsEntityError(e.getMessage());
		}

		return new ResponseEntity<>(meetingResponseDTO, HttpStatus.OK);

	}
}
