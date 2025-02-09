package com.yoprogramoenjava.domain.service.impl;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.yoprogramoenjava.application.exception.AppException;
import com.yoprogramoenjava.domain.model.User;
import com.yoprogramoenjava.domain.service.UsersService;
import com.yoprogramoenjava.infrastructure.db.repository.UsersRepository;

@Service
public class UsersServiceImpl implements UsersService {

	private static final Logger logger = LogManager.getLogger(UsersServiceImpl.class);

	@Autowired
	private UsersRepository usersRepository;

	@Override
	public Optional<User> authenticate(String username, String password) throws AppException {

		if (!StringUtils.hasLength(username) || !StringUtils.hasLength(password))
			throw new AppException(HttpStatus.BAD_REQUEST.value(), "Error. 'username' and 'password' are mandatory parameters");

		Optional<User> user = null;
		try {
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);
			String encryptedPassword = encoder.encode(password);
	
			user = usersRepository.findByUsernameAndPassword(username, encryptedPassword);

			if (user.isPresent())
				logger.info("User '{}' authenticated");
			else
				logger.info("User '{}' not authenticated");
		} catch (Exception e) {
			String errorMsg = new StringBuilder("Error. Something goes wrong during user authentication. username: '")
							.append(username).append("': ").append(e.getMessage()).toString();
			logger.error(errorMsg, e);
			throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR.value(), errorMsg);
		}

		return user;
	}
}
