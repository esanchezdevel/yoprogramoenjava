package com.programandoconjava.presentation.dto;

public record ProductDTO (String id, 
						String name, 
						String type, 
						String description, 
						String metaDescription, 
						String imagePreview, 
						String videoPreview, 
						String filename, 
						String price,
						String currency,
						String tax) {}
