package com.yoprogramoenjava.domain.service;

public interface HtmlParserService {

	/**
	 * Parse the custom tags in one String to HTML tags.
	 * For example: [b][/b] to <b></b>
	 * 
	 * @param content The content to be parsed
	 * @return The content parsed
	 */
	String parseToHtml(String content);
}
