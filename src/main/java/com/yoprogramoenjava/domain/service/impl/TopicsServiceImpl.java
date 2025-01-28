package com.yoprogramoenjava.domain.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yoprogramoenjava.domain.model.Topic;
import com.yoprogramoenjava.domain.service.TopicsService;
import com.yoprogramoenjava.infrastructure.db.repository.TopicsRepository;

@Service
public class TopicsServiceImpl implements TopicsService {

	@Autowired
	private TopicsRepository topicsRepository;


	@Override
	public List<Topic> getAll() {
		return topicsRepository.findAll();
	}


	@Override
	public Optional<Topic> getByTitle(String title) {
		return topicsRepository.findByTitle(title);
	}


	@Override
	public void store(Topic topic) {
		topicsRepository.save(topic);
	}
}
