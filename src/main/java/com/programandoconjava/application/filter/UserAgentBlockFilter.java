package com.programandoconjava.application.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.programandoconjava.domain.model.StatBot;
import com.programandoconjava.domain.service.StatBotService;
import com.programandoconjava.infrastructure.db.repository.StatsBotsRepository;

import java.io.IOException;
import java.util.List;

/**
 * Class used to filter blocked user agents to avoid malicious traffic from known user agents
 */
@Component
public class UserAgentBlockFilter implements Filter {

    private final Logger logger = LogManager.getLogger(UserAgentBlockFilter.class);

    @Autowired
    private StatBotService statBotService;

    // List of user agents blocked because seems to be malicious, not just bots from search engines.
    private static final List<String> BLOCKED_USER_AGENTS = List.of("Custom-AsyncHttpClient", "python-requests", "Nmap Scripting Engine", "l9explore");

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Get the User-Agent header
        String userAgent = httpRequest.getHeader("User-Agent");
        String endpoint = httpRequest.getRequestURI();
        String ip = httpRequest.getRemoteAddr();
        String method = httpRequest.getMethod();

        // Check if the User-Agent matches the one of the blocked ones
        for (String blockedUserAgent : BLOCKED_USER_AGENTS) {
            if (userAgent != null && userAgent.toLowerCase().contains(blockedUserAgent.toLowerCase())) {
                logger.info("Blocked possible Malicious traffic detected from userAgent '{}'", userAgent);
                // Block the request by sending a 403 Forbidden status
                statBotService.logStat(ip, method, endpoint, userAgent);

                httpRequest.setAttribute("fromMaliciousRequest", true);
                httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied for User-Agent: " + userAgent);
                return; // Stop further processing
            }
        }

        // Block traffic that tries to access to forbidden endpoints
        if (endpoint.toLowerCase().contains(".php") || 
            endpoint.toLowerCase().contains("wordpress") || 
            endpoint.toLowerCase().contains("wp-admin") || 
            endpoint.toLowerCase().contains(".env") || 
            endpoint.toLowerCase().contains("cmd_sco") ||
            endpoint.toLowerCase().contains("AwsConfig.json") ||
            endpoint.toLowerCase().contains("user_secrets.yml") ||
            endpoint.toLowerCase().contains("phpinfo") ||
            endpoint.toLowerCase().contains("HNAP1")) {
            logger.info("Blocked possible Malicious traffic detected from userAgent '{}' trying to access to endpoint: {}", userAgent, endpoint);
            // Block the request by sending a 403 Forbidden status
            statBotService.logStat(ip, method, endpoint, userAgent);

            httpRequest.setAttribute("fromMaliciousRequest", true);
            httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied for User-Agent: " + userAgent + " in endpoint " + endpoint);
            return; // Stop further processing
        }

        // If the User-Agent does not match, continue with the filter chain
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Clean up resources if needed
    }
}