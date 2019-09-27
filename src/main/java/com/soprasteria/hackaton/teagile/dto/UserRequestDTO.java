package com.soprasteria.hackaton.teagile.dto;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Request DTO for User
 * 
 * @author Igor Dosinchuk
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@ApiModel(reference = "UserRequest", description = "Model request for User.")
public class UserRequestDTO {

	@Id
	@ApiModelProperty(value = "Id", example = "1")
	private int id;

	@ApiModelProperty(value = "User name", example = "Jhon")
	private String name;

	@ApiModelProperty(value = "User surname", example = "Stevenson")
	private String surname;

	@ApiModelProperty(value = "Login name", example = "jhon")
	private String loginName;

	@ApiModelProperty(value = "Login password", example = "admin123")
	private String loginPassword;

}
