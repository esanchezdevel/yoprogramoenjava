package com.programandoconjava.infrastructure.db.dto;

import java.time.LocalDateTime;

public record ArticleDTO (Long id, String title, String description, String tags, LocalDateTime dateCreation, boolean published) { }
