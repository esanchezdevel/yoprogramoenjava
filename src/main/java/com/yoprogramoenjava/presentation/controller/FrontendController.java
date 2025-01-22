package com.yoprogramoenjava.presentation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.yoprogramoenjava.application.utils.Constants;

@Controller
public class FrontendController {

	@GetMapping("/")
	public String getIndex(Model model) {
		
		model.addAttribute(Constants.ATTRIBUTE_NAME_TITLE, Constants.ATTRIBUTE_VALUE_TITLE);
		
		return "index";
	}
}
