package com.yoprogramoenjava.infrastructure.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yoprogramoenjava.application.utils.Constants;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@GetMapping() 
	public String getAdminPanel(Model model) {
		model.addAttribute(Constants.ATTRIBUTE_NAME_TITLE, Constants.ATTRIBUTE_VALUE_TITLE);
		
		return "admin/index";
	}
	
	@GetMapping("/articles")
	public String getArticles(Model model) {
		model.addAttribute(Constants.ATTRIBUTE_NAME_TITLE, Constants.ATTRIBUTE_VALUE_TITLE);
		
		return "admin/articles";
	}
}
