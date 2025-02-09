package com.yoprogramoenjava.domain.service.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.yoprogramoenjava.application.exception.AppException;
import com.yoprogramoenjava.domain.model.ExternalNew;
import com.yoprogramoenjava.domain.service.ExternalNewsService;

public class ExternalNewsServiceImpl implements ExternalNewsService {

	private static final Logger logger = LogManager.getLogger(ExternalNewsServiceImpl.class);

	@Override
	public List<ExternalNew> getAll() throws AppException {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void store(ExternalNew externalNew) throws AppException {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void update(Long id, ExternalNew externalNew) throws AppException {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void delete(Long id) throws AppException {
		// TODO Auto-generated method stub
		
	}
}
