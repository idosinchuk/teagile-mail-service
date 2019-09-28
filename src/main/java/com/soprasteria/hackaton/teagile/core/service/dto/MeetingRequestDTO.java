package com.soprasteria.hackaton.teagile.core.service.dto;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Request DTO for Meeting
 * 
 * @author Igor Dosinchuk
 * @author Luis Rapestre
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@ApiModel(reference = "MeetingRequest", description = "Model request for Meeting.")
public class MeetingRequestDTO {

	@Id
	@ApiModelProperty(value = "Id", example = "1")
	private int id;

	@NotNull
	@ApiModelProperty(value = "Meeting title", example = "TEAgile")
	private String title;

	@ApiModelProperty(value = "Meeting description", example = "This is a description")
	private String description;

	@ApiModelProperty(value = "Start date", example = "27-09-2019 17:30:00")
	private String startDate;

	@ApiModelProperty(value = "End date", example = "28-09-2019 14:00:00")
	private String endDate;

	@ApiModelProperty(value = "Event repetition frequency", example = "5")
	private int eventRepeatFrequency;

	@ApiModelProperty(value = "Priority", example = "1")
	private int priority;

	@ApiModelProperty(value = "Meeting reminder in minutes", example = "60")
	private int reminder;

	@ApiModelProperty(value = "Meeting status", example = "Created")
	private String status;

	@ApiModelProperty(value = "Project Id", example = "1")
	private int projectId;

}
