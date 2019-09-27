package com.soprasteria.hackaton.teagile.core.service.dto;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Request DTO for project
 * 
 * @author Igor Dosinchuk
 * @author Luis Rapestre
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@ApiModel(reference = "ProjectRequest", description = "Model request for project.")
public class ProjectRequestDTO {

	@Id
	@ApiModelProperty(value = "Id", example = "1")
	private int id;

	@NotNull
	@ApiModelProperty(value = "Project name", example = "TEAgile")
	private String name;

	@ApiModelProperty(value = "Project status", example = "Created")
	private String status;

}