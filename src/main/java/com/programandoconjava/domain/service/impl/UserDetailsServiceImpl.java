package com.programandoconjava.domain.service.impl;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.programandoconjava.domain.model.User;
import com.programandoconjava.infrastructure.db.repository.UsersRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private static final Logger logger = LogManager.getLogger(UserDetailsServiceImpl.class);

	@Autowired
	private UsersRepository usersRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Optional<User> user = usersRepository.findByUsername(username);

		if (user.isEmpty()) {
			logger.info("User '{}' not found", username);
			throw new UsernameNotFoundException(new StringBuilder("Error. User  '")
																	.append(username)
																	.append("' not found").toString());
		}
		logger.info("User '{}' found", username);
		
		return org.springframework.security.core.userdetails.User
			.withUsername(user.get().getUsername())
			.password(user.get().getPassword())
			.roles(user.get().getRole().value())
			.build();
	}
}
