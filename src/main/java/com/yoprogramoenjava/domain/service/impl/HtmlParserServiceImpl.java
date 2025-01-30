package com.yoprogramoenjava.domain.service.impl;

import org.springframework.stereotype.Service;

import com.yoprogramoenjava.domain.service.HtmlParserService;

@Service
public class HtmlParserServiceImpl implements HtmlParserService {

	@Override
	public String parseToHtml(String content) {

		String parsedContent = content.replace("[b]", "<b>");
		parsedContent = parsedContent.replace("[/b]", "</b>");
		parsedContent = parsedContent.replace("[i]", "<i>");
		parsedContent = parsedContent.replace("[/i]", "</i>");
		parsedContent = parsedContent.replace("[img src='", "<img src=\"");
		parsedContent = parsedContent.replace("']", "\">");
		parsedContent = parsedContent.replace("[code]", "</p><pre><code>");
		parsedContent = parsedContent.replace("[/code]", "</code></pre><p>");
		
		return parsedContent;
	}
}
