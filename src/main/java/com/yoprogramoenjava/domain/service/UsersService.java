package com.yoprogramoenjava.domain.service;

import java.util.Optional;

import com.yoprogramoenjava.application.exception.AppException;
import com.yoprogramoenjava.domain.model.User;

public interface UsersService {

	/**
	 * Authenticate one user in base of the username and the password received
	 * 
	 * @param username The Username to look for
	 * @param password The Password to look for
	 * @return Optional of the User. Empty if no user was found
	 * @throws AppException When any error happens
	 */
	Optional<User> authenticate(String username, String password) throws AppException;
}
