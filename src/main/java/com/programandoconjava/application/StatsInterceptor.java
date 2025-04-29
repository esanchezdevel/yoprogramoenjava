package com.programandoconjava.application;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.programandoconjava.domain.model.Stat;
import com.programandoconjava.domain.model.StatBot;
import com.programandoconjava.domain.service.StatBotService;
import com.programandoconjava.infrastructure.db.repository.StatsBotsRepository;
import com.programandoconjava.infrastructure.db.repository.StatsRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class StatsInterceptor implements HandlerInterceptor {

	@Autowired
    private StatsRepository statsRepository;

    @Autowired
    private StatBotService statBotService;

    private static final List<String> BOTS_IDENTIFIERS = List.of("Googlebot", "Bingbot", "Slurp", "DuckDuckBot",
            "Baiduspider", "YandexBot", "Sogou", "Exabot", "facebot", "ia_archiver", "censys", "zgrab", "Go-http-client", "onlyscans.com",
            "twitter", "Crawler", "Custom-AsyncHttpClient");

    @SuppressWarnings("null")
	@Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String ip = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");
		String method = request.getMethod();
        String endpoint = request.getRequestURI();

        // When the request is to /error endpoint but comes from the a Forbidden traffic, not store in stats.
        if ("/error".equals(endpoint) && Boolean.TRUE.equals(request.getAttribute("fromMaliciousRequest"))) {
            return true;
        }

        if (!isBot(userAgent)) {
            Stat stat = new Stat();
            stat.setIp(ip);
            stat.setUserAgent(userAgent);
            stat.setMethod(method);
            stat.setEndpoint(endpoint);
    
            statsRepository.save(stat);
        } else {
            statBotService.logStat(ip, method, endpoint, userAgent);
        }
        return true;
    }

    private boolean isBot(String userAgent) {
        boolean isBot = false;
        for (String identifier : BOTS_IDENTIFIERS) {
            if (userAgent != null && userAgent.toLowerCase().contains(identifier.toLowerCase())) {
                isBot = true;
                break;
            }
        }
        return isBot;
    }
}