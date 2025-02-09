package com.yoprogramoenjava.domain.service.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

import com.yoprogramoenjava.application.exception.AppException;
import com.yoprogramoenjava.domain.model.ExternalNew;
import com.yoprogramoenjava.domain.service.ExternalNewsService;
import com.yoprogramoenjava.infrastructure.db.repository.ExternalNewsRepository;

public class ExternalNewsServiceImpl implements ExternalNewsService {

	private static final Logger logger = LogManager.getLogger(ExternalNewsServiceImpl.class);

	@Autowired
	private ExternalNewsRepository externalNewsRepository;

	@Override
	public List<ExternalNew> getAll() throws AppException {
		try {
			return externalNewsRepository.findAll();
		} catch (Exception e) {
			String errorMsg = new StringBuilder("Error getting ExternalNews from database. ").append(e.getMessage()).toString();
			logger.error(errorMsg, e);
			throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR.value(), errorMsg);
		}
	}


	@Override
	public void store(ExternalNew externalNew) throws AppException {
		if (!isValid(externalNew)) {
			String errorMsg = new StringBuilder("Error. Wrong ExternalNew object received: ").append(externalNew).toString();
			logger.error(errorMsg);
			throw new AppException(HttpStatus.BAD_REQUEST.value(), errorMsg);
		}

		try {
			externalNewsRepository.save(externalNew);
		} catch (Exception e) {
			String errorMsg = new StringBuilder("Error storing ExternalNews from database. ").append(e.getMessage()).toString();
			logger.error(errorMsg, e);
			throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR.value(), errorMsg);
		}
	}


	@Override
	public void update(Long id, ExternalNew externalNew) throws AppException {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void delete(Long id) throws AppException {
		// TODO Auto-generated method stub
		
	}


	private boolean isValid(ExternalNew externalNew) {
		return externalNew != null && 
			StringUtils.hasLength(externalNew.getTitle()) && 
			StringUtils.hasLength(externalNew.getSource()) && 
			StringUtils.hasLength(externalNew.getLink());
	}
}
