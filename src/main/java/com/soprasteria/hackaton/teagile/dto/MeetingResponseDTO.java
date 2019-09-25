package com.soprasteria.hackaton.teagile.dto;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Response DTO for Meeting
 * 
 * @author Igor Dosinchuk
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@ApiModel(reference = "MeetingResponse", description = "Model response for Meeting.")
public class MeetingResponseDTO {

	@Id
	@ApiModelProperty(value = "Id", example = "1")
	private int id;

	@ApiModelProperty(value = "Meeting title", example = "TEAgile")
	private String title;

	@ApiModelProperty(value = "Meeting description", example = "This is a description")
	private String description;

	@ApiModelProperty(value = "Target user", example = "Jhon")
	private String targetUser;

	@ApiModelProperty(value = "Start date", example = "27-09-2019 17:30:00")
	private String startDate;

	@ApiModelProperty(value = "End date", example = "28-09-2019 14:00:00")
	private String endDate;

	@ApiModelProperty(value = "Event repetition frequency", example = "5")
	private int eventRepeatFrequency;

	@ApiModelProperty(value = "Meeting label color", example = "blue")
	private String labelColor;

	@ApiModelProperty(value = "Meeting reminder in minutes", example = "60")
	private int reminder;

	@ApiModelProperty(value = "Meeting status", example = "Created")
	private String status;

}
