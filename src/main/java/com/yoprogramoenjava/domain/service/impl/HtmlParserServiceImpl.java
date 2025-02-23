package com.yoprogramoenjava.domain.service.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.yoprogramoenjava.domain.service.HtmlParserService;

@Service
public class HtmlParserServiceImpl implements HtmlParserService {

	private static final Logger logger = LogManager.getLogger(HtmlParserServiceImpl.class);

	@Override
	public String parseToHtml(String content) {

		String parsedContent = content.replace("[b]", "<b>");
		parsedContent = parsedContent.replace("[/b]", "</b>");
		parsedContent = parsedContent.replace("[i]", "<i>");
		parsedContent = parsedContent.replace("[/i]", "</i>");
		parsedContent = parsedContent.replace("[img src='", "<img class=\"article-image\" src=\"");
		parsedContent = parsedContent.replace("']", "\">");
		parsedContent = parsedContent.replace("[code]", "</p><pre class=\"code-block\"><code>");
		parsedContent = parsedContent.replace("[/code]", "</code></pre><p>");

		logger.info("TEST--parsedContent: {}", parsedContent);

		parsedContent = parseNewLines(parsedContent);

		return parsedContent;
	}

	private String parseNewLines(String text) {
		// Only replace new lines \n with <br> out of the blocks of code
		Pattern pattern = Pattern.compile("(?i)<code>.*?</code>", Pattern.DOTALL);
		Matcher matcher = pattern.matcher(text);

        // Buffer to store the final result
        StringBuilder result = new StringBuilder();

        // Replace newlines outside <code>...</code>
        int lastEnd = 0;
        while (matcher.find()) {
            // Replace newlines in the part before this <code> block
            String beforeBlock = text.substring(lastEnd, matcher.start()).replace("\n", "<br>");
            result.append(beforeBlock);

            // Append the <code> block unchanged
            result.append(matcher.group());

            // Update lastEnd to continue processing
            lastEnd = matcher.end();
        }

        // Replace newlines in the remaining text after the last <code> block
        result.append(text.substring(lastEnd).replace("\n", "<br>"));

        return result.toString();
	}
}
