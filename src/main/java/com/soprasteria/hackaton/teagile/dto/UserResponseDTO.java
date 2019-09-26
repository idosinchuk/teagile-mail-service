package com.soprasteria.hackaton.teagile.dto;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Response DTO for User
 * 
 * @author Igor Dosinchuk
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@ApiModel(reference = "UserResponse", description = "Model response for User.")
public class UserResponseDTO {

	@Id
	@ApiModelProperty(value = "Id", example = "1")
	private int id;

	@ApiModelProperty(value = "User name", example = "Jhon")
	private String name;

	@ApiModelProperty(value = "User surname", example = "Stevenson")
	private String surname;

	@ApiModelProperty(value = "Projects", example = "")
	private ProjectResponseDTO projects;

}