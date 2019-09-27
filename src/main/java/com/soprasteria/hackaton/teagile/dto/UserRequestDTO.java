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
 * @author Luis Rapestre
 * 
 * 
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

	@ApiModelProperty(value = "Email", example = "jhon")
	private String email;

	@ApiModelProperty(value = "Login password", example = "admin123")
	private String password;

}
