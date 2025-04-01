package com.yoprogramoenjava.infrastructure.db.dto;

import java.time.LocalDateTime;

public record ArticleDTO (Long id, String title, String description, LocalDateTime dateCreation, boolean published) { }
