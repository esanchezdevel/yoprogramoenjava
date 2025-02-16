package com.yoprogramoenjava.presentation.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yoprogramoenjava.application.utils.Constants;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {

	private static final Logger logger = LogManager.getLogger(CustomErrorController.class);

	@RequestMapping("/error")
	public String handleError(HttpServletRequest request, Model model) {
		logger.info("Custom error page");
		model.addAttribute(Constants.ATTRIBUTE_NAME_TITLE, Constants.ATTRIBUTE_VALUE_TITLE);

		return "error";
	}

}
