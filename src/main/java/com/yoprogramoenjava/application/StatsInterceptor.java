package com.yoprogramoenjava.application;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.yoprogramoenjava.domain.model.Stat;
import com.yoprogramoenjava.infrastructure.db.repository.StatsRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class StatsInterceptor implements HandlerInterceptor {

	@Autowired
    private StatsRepository statsRepository;

    private static final List<String> BOTS_IDENTIFIERS = List.of("Googlebot", "Bingbot", "Slurp", "DuckDuckBot",
            "Baiduspider", "YandexBot", "Sogou", "Exabot", "facebot", "ia_archiver");

    @SuppressWarnings("null")
	@Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String ip = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");
		String method = request.getMethod();
        String endpoint = request.getRequestURI();

        if (!isBot(userAgent)) {
            Stat stat = new Stat();
            stat.setIp(ip);
            stat.setUserAgent(userAgent);
            stat.setMethod(method);
            stat.setEndpoint(endpoint);
    
            statsRepository.save(stat);
        }
        return true;
    }

    private boolean isBot(String userAgent) {
        boolean isBot = false;
        for (String identifier : BOTS_IDENTIFIERS) {
            if (userAgent.contains(identifier)) {
                isBot = true;
                break;
            }
        }
        return isBot;
    }
}