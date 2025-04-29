package com.programandoconjava.domain.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.programandoconjava.domain.model.StatBot;
import com.programandoconjava.domain.service.StatBotService;
import com.programandoconjava.infrastructure.db.repository.StatsBotsRepository;

@Service
public class StatBotServiceImpl implements StatBotService {

	@Autowired
	private StatsBotsRepository statsBotsRepository;

	@Override
	public void logStat(String ip, String method, String endpoint, String userAgent) {
		StatBot statBot = new StatBot();
		statBot.setIp(ip);
		statBot.setUserAgent(userAgent);
		statBot.setMethod(method);
		statBot.setEndpoint(endpoint);

		statsBotsRepository.save(statBot);
	}
}
