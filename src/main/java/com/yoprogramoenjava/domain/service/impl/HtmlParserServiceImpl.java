package com.yoprogramoenjava.domain.service.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
		parsedContent = parsedContent.replace("[h3]", "<h3 class=\"article-h3\">");
		parsedContent = parsedContent.replace("[/h3]", "</h3>");
		parsedContent = parsedContent.replace("[img src='", "<img class=\"article-image\" src=\"");
		parsedContent = parsedContent.replace("']", "\">");
		parsedContent = parsedContent.replace("[code]", "</p><pre class=\"code-block\"><code>");
		parsedContent = parsedContent.replace("[/code]", "</code></pre><p>");
		parsedContent = parsedContent.replace("[youtube src='", "<div class=\"video-wrapper\"><iframe width=\"560\" height=\"315\" src=\"");
		parsedContent = parsedContent.replace("' youtube]", "\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe></div>");

		parsedContent = parseNewLines(parsedContent);
		parsedContent = removeNewLineAfterHeader(parsedContent);

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

	private String removeNewLineAfterHeader(String text) {
		return text.replaceAll("</h3>\\s*<br>\\s*<br>", "</h3>\n</br>");
	}
}
