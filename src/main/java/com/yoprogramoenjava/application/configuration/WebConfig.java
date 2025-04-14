package com.yoprogramoenjava.application.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.yoprogramoenjava.application.StatsInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer{

	@Autowired
	private StatsInterceptor statsInterceptor;

	@SuppressWarnings("null")
	@Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(statsInterceptor)
                .addPathPatterns("/**")
				.excludePathPatterns(
                        "/css/**",
                        "/js/**",
                        "/images/**",
                        "/webjars/**",
                        "/favicon.ico",
						"/img/favicon.ico",
                        "/**/*.png",
                        "/**/*.jpg",
                        "/**/*.jpeg",
                        "/**/*.gif",
                        "/**/*.svg",
                        "/**/*.js",
                        "/**/*.css"
                );
    }
}
