package com.soprasteria.hackaton.teagile.core.service.dto;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Response DTO for Task
 * 
 * @author Igor Dosinchuk
 * @author Luis Rapestre
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@ApiModel(reference = "TaskResponse", description = "Model response for Task.")
public class TaskResponseDTO {

	@Id
	@ApiModelProperty(value = "Id", example = "1")
	private int id;

	@ApiModelProperty(value = "Task title", example = "TEAgile")
	private String title;

	@ApiModelProperty(value = "Task description", example = "This is a description")
	private String description;

	@ApiModelProperty(value = "Start date", example = "27-09-2019 17:30:00")
	private String startDate;

	@ApiModelProperty(value = "End date", example = "28-09-2019 14:00:00")
	private String endDate;

	@ApiModelProperty(value = "Task label color", example = "blue")
	private String labelColor;

	@ApiModelProperty(value = "Task status", example = "Created")
	private String status;
	
	@ApiModelProperty(value = "Project Id", example = "1")
	private String projectId;

}
