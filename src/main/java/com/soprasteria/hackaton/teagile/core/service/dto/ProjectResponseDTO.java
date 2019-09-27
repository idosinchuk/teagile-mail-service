package com.soprasteria.hackaton.teagile.core.service.dto;

import java.util.List;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Response DTO for project
 * 
 * @author Igor Dosinchuk
 * @author Luis Rapestre
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@ApiModel(reference = "ProjectResponse", description = "Model response for project.")
public class ProjectResponseDTO {

	@Id
	@ApiModelProperty(value = "Id", example = "1")
	private int id;

	@ApiModelProperty(value = "Project name", example = "TEAgile")
	private String name;

	@ApiModelProperty(value = "Project status", example = "Created")
	private String status;

	@ApiModelProperty(value = "Tasks", example = "")
	private List<TaskResponseDTO> tasks;

	@ApiModelProperty(value = "Meetings", example = "")
	private List<MeetingResponseDTO> meetings;

}