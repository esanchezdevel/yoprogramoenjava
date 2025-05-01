package com.programandoconjava.presentation.dto.mapping;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import com.programandoconjava.presentation.dto.TagDTO;

public class ArticleTagsMapping {

	public static Set<TagDTO> parseToSetOfDTOs(Set<String> tags) {
		Set<TagDTO> dtos = new HashSet<>();
		tags.forEach(tag -> {
			Random random = new Random();
			int fontSize = random.nextInt(12, 35);

			dtos.add(new TagDTO(tag, String.valueOf(fontSize)));
		});
		return dtos;
	}
}
