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
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * Class used to filter blocked user agents to avoid malicious traffic from known user agents
 */
@Component
public class UserAgentBlockFilter implements Filter {

    private final Logger logger = LogManager.getLogger(UserAgentBlockFilter.class);

    // List of user agents blocked
    private static final List<String> BLOCKED_USER_AGENTS = List.of("Custom-AsyncHttpClient");

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

        // Check if the User-Agent matches the one of the blocked ones
        for (String blockedUserAgent : BLOCKED_USER_AGENTS) {
            if (userAgent != null && userAgent.toLowerCase().contains(blockedUserAgent.toLowerCase())) {
                logger.info("Blocked possible Malicious traffic detected from userAgent '{}'", userAgent);
                // Block the request by sending a 403 Forbidden status
                httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied for User-Agent: " + userAgent);
                return; // Stop further processing
            }
        }
        // If the User-Agent does not match, continue with the filter chain
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Clean up resources if needed
    }
}