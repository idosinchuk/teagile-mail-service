package com.soprasteria.hackaton.teagile.dto;

import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Request DTO for project
 * 
 * @author Igor Dosinchuk
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@ApiModel(reference = "ProjectRequest", description = "Model request for project.")
public class ProjectRequestDTO {

	@Id
	@ApiModelProperty(value = "Id", example = "1")
	private int id;

	@ApiModelProperty(value = "Project name", example = "TEAgile")
	private String name;

	@ApiModelProperty(value = "Project status", example = "Created")
	private String status;

}