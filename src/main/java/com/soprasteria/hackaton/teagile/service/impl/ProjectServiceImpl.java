package com.soprasteria.hackaton.teagile.service.impl;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.io.Resources;
import com.google.common.reflect.TypeToken;
import com.soprasteria.hackaton.teagile.controller.ProjectController;
import com.soprasteria.hackaton.teagile.dto.ProjectResponseDTO;
import com.soprasteria.hackaton.teagile.service.ProjectService;

import springfox.documentation.swagger2.mappers.ModelMapper;

/**
 * Implementation for project service
 * 
 * @author Igor Dosinchuk
 *
 */
@Service("ProjectService")
public class ProjectServiceImpl implements ProjectService {

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private VehicleRepository vehicleRepository;

	@Autowired
	private HolderRepository holderRepository;

	@Autowired
	private ModelMapper modelMapper;

	public static final Logger logger = LoggerFactory.getLogger(ProjectServiceImpl.class);

	/**
	 * {@inheritDoc}
	 */
	public List<ProjectResponseDTO> getAllProjects(Pageable pageable) {

		List<ProjectEntity> entityResponse = projectRepository.findAll(pageable);

		// Convert Entity response to DTO
		return modelMapper.map(entityResponse, new TypeToken<List<ProjectResponseDTO>>() {
		}.getType());

	}

	/**
	 * {@inheritDoc}
	 */
	public ProjectResponseDTO getProjects(String projectCode) {

		ProjectEntity entityResponse = projectRepository.findById(projectCode);

		return modelMapper.map(entityResponse, ProjectResponseDTO.class);

	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional
	public ResponseEntity<?> addProject(ProjectRequestDTO projectRequestDTO) {

		Resources<CustomMessage> resource = null;

		try {
			List<CustomMessage> customMessageList = null;

			ProjectEntity entityRequest = modelMapper.map(projectRequestDTO, ProjectEntity.class);

			ProjectEntity projectEntity = projectRepository.findById(projectRequestDTO.getProjectCode());

			// Check if projectCode exists in the database
			if (projectEntity != null) {
				customMessageList = ArrayListCustomMessage.setMessage(
						"The requested project actually exists. Please change projectCode.", HttpStatus.BAD_REQUEST);
				resource = new Resources<>(customMessageList);
				resource.add(linkTo(ProjectController.class).withSelfRel());

				return new ResponseEntity<>(resource, HttpStatus.BAD_REQUEST);
			}

			// Check if product exists in the database
			ProductEntity productEntity = productRepository.findByProductCode(projectRequestDTO.getProductCode());

			// Check if holder exists in the database
			HolderEntity holderEntity = holderRepository.findByPassportNumber(projectRequestDTO.getPassportNumber());

			// Check if vehicle exists in the database
			VehicleEntity vehicleEntity = vehicleRepository.findByLicensePlate(projectRequestDTO.getLicensePlate());

			if (productEntity != null && holderEntity != null && vehicleEntity != null) {
				entityRequest.setProduct(productEntity);
				entityRequest.setHolder(holderEntity);
				entityRequest.setVehicle(vehicleEntity);
			} else {
				customMessageList = ArrayListCustomMessage.setMessage("Some of the requested data are not correct",
						HttpStatus.BAD_REQUEST);
				resource = new Resources<>(customMessageList);
				resource.add(linkTo(ProjectController.class).withSelfRel());
				return new ResponseEntity<>(resource, HttpStatus.BAD_REQUEST);
			}

			projectRepository.save(entityRequest);

			customMessageList = ArrayListCustomMessage.setMessage("Created new project", HttpStatus.CREATED);

			resource = new Resources<>(customMessageList);
			resource.add(linkTo(ProjectController.class).withSelfRel());
		} catch (Exception e) {
			logger.error("An error occurred! {}", e.getMessage());
			return CustomErrorType.returnResponsEntityError(e.getMessage());
		}

		return new ResponseEntity<>(resource, HttpStatus.OK);

	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional
	public ResponseEntity<?> updateProject(String projectCode, ProjectRequestDTO projectRequestDTO) {

		Resources<CustomMessage> resource = null;

		try {

			List<CustomMessage> customMessageList = null;

			customMessageList = ArrayListCustomMessage.setMessage("Patch project process", HttpStatus.OK);

			// Find project by project code for check if exists in DB
			ProjectEntity projectEntity = projectRepository.findById(projectCode);

			// If exists
			if (projectEntity != null) {

				// The project's code and ID will always be the same, so we do not allow it to
				// be
				// updated, for them we overwrite the field with the original value.
				projectRequestDTO.setProjectCode(projectCode);
				projectRequestDTO.setId(projectEntity.getId());

				ProjectEntity entityRequest = modelMapper.map(projectRequestDTO, ProjectEntity.class);

				if (projectRequestDTO.getProductCode() != null && !projectRequestDTO.getProductCode().isEmpty()) {

					ProductEntity productEntity = productRepository
							.findByProductCode(projectRequestDTO.getProductCode());

					// Check if product exists in the database
					if (productEntity != null) {
						entityRequest.setProduct(productEntity);
					} else {
						customMessageList = ArrayListCustomMessage.setMessage(
								"Product code " + projectRequestDTO.getProductCode() + " does not exist!",
								HttpStatus.BAD_REQUEST);
						resource = new Resources<>(customMessageList);
						resource.add(linkTo(ProjectController.class).withSelfRel());
						return new ResponseEntity<>(resource, HttpStatus.BAD_REQUEST);
					}
				}

				if (projectRequestDTO.getPassportNumber() != null && !projectRequestDTO.getProductCode().isEmpty()) {
					HolderEntity holderEntity = holderRepository
							.findByPassportNumber(projectRequestDTO.getPassportNumber());

					// Check if holder exists in the database
					if (holderEntity != null) {
						entityRequest.setHolder(holderEntity);
					} else {
						customMessageList = ArrayListCustomMessage.setMessage(
								"Holder passport number " + projectRequestDTO.getPassportNumber() + " does not exist!",
								HttpStatus.BAD_REQUEST);
						resource = new Resources<>(customMessageList);
						resource.add(linkTo(ProjectController.class).withSelfRel());
						return new ResponseEntity<>(resource, HttpStatus.BAD_REQUEST);
					}
				}

				if (projectRequestDTO.getLicensePlate() != null && !projectRequestDTO.getProductCode().isEmpty()) {
					VehicleEntity vehicleEntity = vehicleRepository
							.findByLicensePlate(projectRequestDTO.getLicensePlate());

					// Check if vehicle exists in the database
					if (vehicleEntity != null) {
						entityRequest.setVehicle(vehicleEntity);
					} else {
						customMessageList = ArrayListCustomMessage.setMessage(
								"Vehicle license plate " + projectRequestDTO.getLicensePlate() + " does not exist!",
								HttpStatus.BAD_REQUEST);
						resource = new Resources<>(customMessageList);
						resource.add(linkTo(ProjectController.class).withSelfRel());
						return new ResponseEntity<>(resource, HttpStatus.BAD_REQUEST);
					}
				}

				// Check if there are changes
				if (!projectEntity.equals(entityRequest)) {
					projectRepository.save(entityRequest);
				} else {
					customMessageList = ArrayListCustomMessage.setMessage("There are no changes, please try again",
							HttpStatus.BAD_REQUEST);

					resource = new Resources<>(customMessageList);
					resource.add(linkTo(HolderController.class).withSelfRel());

					return new ResponseEntity<>(resource, HttpStatus.BAD_REQUEST);
				}

			} else {
				customMessageList = ArrayListCustomMessage.setMessage(
						"Project code" + projectRequestDTO.getProjectCode() + " Not Found!", HttpStatus.BAD_REQUEST);

				resource = new Resources<>(customMessageList);
				resource.add(linkTo(ProjectController.class).withSelfRel());

				return new ResponseEntity<>(resource, HttpStatus.BAD_REQUEST);
			}

			resource = new Resources<>(customMessageList);
			resource.add(linkTo(ProjectController.class).slash(projectRequestDTO.getProjectCode()).withSelfRel());
		} catch (Exception e) {
			logger.error("An error occurred! {}", e.getMessage());
			return CustomErrorType.returnResponsEntityError(e.getMessage());

		}

		return new ResponseEntity<>(resource, HttpStatus.OK);

	}
}
