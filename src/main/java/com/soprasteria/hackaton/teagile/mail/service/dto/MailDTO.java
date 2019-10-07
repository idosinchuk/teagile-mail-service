package com.soprasteria.hackaton.teagile.mail.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class MailDTO {

	private String email;

	private String description;



}
