package com.soprasteria.hackaton.teagile.core.service.service.impl;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import java.util.Base64;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.soprasteria.hackaton.teagile.core.service.common.ArrayListCustomMessage;
import com.soprasteria.hackaton.teagile.core.service.common.CustomErrorType;
import com.soprasteria.hackaton.teagile.core.service.common.CustomMessage;
import com.soprasteria.hackaton.teagile.core.service.controller.UserController;
import com.soprasteria.hackaton.teagile.core.service.entity.UserEntity;
import com.soprasteria.hackaton.teagile.core.service.repository.UserRepository;
import com.soprasteria.hackaton.teagile.core.service.service.LoginService;

@Service("LoginService")
public class LoginServiceImpl implements LoginService {

	@Autowired
	private UserRepository userRepository;

	public static final Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);

	public ResponseEntity<?> getLogin(String email, String password) {

		Resources<CustomMessage> resource = null;

		try {
			List<CustomMessage> customMessageList = null;

			byte[] passwordBytes = Base64.getDecoder().decode(password);
			String decodedPassword = new String(passwordBytes);

			UserEntity entityResponse = userRepository.findByEmailAndPassword(email, decodedPassword);

			if (entityResponse == null) {
				customMessageList = ArrayListCustomMessage
						.setMessage("The requested user does not exists. Please try again.", HttpStatus.NO_CONTENT);
				resource = new Resources<>(customMessageList);
				resource.add(linkTo(UserController.class).withSelfRel());

				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

		} catch (Exception e) {
			logger.error("An error occurred! {}", e.getMessage());
			return CustomErrorType.returnResponsEntityError(e.getMessage());
		}

		return new ResponseEntity<>(resource, HttpStatus.OK);

	}

}
