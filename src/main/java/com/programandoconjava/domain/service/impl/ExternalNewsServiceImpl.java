package com.programandoconjava.domain.service.impl;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.programandoconjava.application.exception.AppException;
import com.programandoconjava.domain.model.ExternalNew;
import com.programandoconjava.domain.service.ExternalNewsService;
import com.programandoconjava.infrastructure.db.repository.ExternalNewsRepository;

import jakarta.transaction.Transactional;

@Service
public class ExternalNewsServiceImpl implements ExternalNewsService {

	private static final Logger logger = LogManager.getLogger(ExternalNewsServiceImpl.class);

	@Autowired
	private ExternalNewsRepository externalNewsRepository;

	@Override
	public List<ExternalNew> getAll() throws AppException {
		try {
			return externalNewsRepository.findAllByOrderByDateCreationDesc();
		} catch (Exception e) {
			String errorMsg = new StringBuilder("Error getting ExternalNews from database. ").append(e.getMessage()).toString();
			logger.error(errorMsg, e);
			throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR.value(), errorMsg);
		}
	}

	@Override
	public Optional<ExternalNew> getById(Long id) throws AppException {
		try {
			return externalNewsRepository.findById(id);
		} catch(Exception e) {
			String errorMsg = new StringBuilder("Unexpected error getting ExternalNew with Id '").append(id).append("': ").append(e.getMessage()).toString();
			logger.error(errorMsg, e);
			throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR.value(), errorMsg);
		}
	}

	@Override
	public List<ExternalNew> getLast() throws AppException {
		try {
			return externalNewsRepository.findTop5ByOrderByDateCreationDesc();
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
	@Transactional
	public void update(Long id, ExternalNew externalNew) throws AppException {
		if (!isValid(externalNew)) {
			String errorMsg = new StringBuilder("Error. Wrong ExternalNew object received: ").append(externalNew).toString();
			logger.error(errorMsg);
			throw new AppException(HttpStatus.BAD_REQUEST.value(), errorMsg);
		}
		
		ExternalNew externalNewDbValue = null;
		try {
			Optional<ExternalNew> externalNewDb = externalNewsRepository.findById(id);

			if (externalNewDb.isEmpty()) {
				String errorMsg = new StringBuilder("Error. ExternalNew with id '").append(id).append("' not found").toString();
				logger.error(errorMsg);
				throw new AppException(HttpStatus.NOT_FOUND.value(), errorMsg);
			}

			externalNewDbValue = externalNewDb.get();
		} catch (Exception e) {
			if (e instanceof AppException) {
				throw e;
			} else {
				String errorMsg = new StringBuilder("Error. Unexpected error happens getting ExternalNew with id '")
														.append(id).append("' from Database").toString();
				logger.error(errorMsg);
				throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR.value(), errorMsg);
			}
		}

		try {
			externalNewDbValue.setTitle(externalNew.getTitle());
			externalNewDbValue.setSource(externalNew.getSource());
			externalNewDbValue.setLink(externalNew.getLink());
		} catch (Exception e) {
			String errorMsg = new StringBuilder("Error updating ExternalNew with id '")
													.append(id).append("': ").append(externalNew).append(". ")
													.append(e.getMessage()).toString();
			logger.error(errorMsg, e);
			throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR.value(), errorMsg);
		}
	}


	@Override
	public void delete(Long id) throws AppException {
		try {
			externalNewsRepository.deleteById(id);
		} catch (Exception e) {
			String errorMsg = new StringBuilder("Error deleting ExternalNew with id '")
													.append(id).append("': ").append(e.getMessage()).toString();
			logger.error(errorMsg, e);
			throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR.value(), errorMsg);
		}
	}


	private boolean isValid(ExternalNew externalNew) {
		return externalNew != null && 
			StringUtils.hasLength(externalNew.getTitle()) && 
			StringUtils.hasLength(externalNew.getSource()) && 
			StringUtils.hasLength(externalNew.getLink());
	}
}
