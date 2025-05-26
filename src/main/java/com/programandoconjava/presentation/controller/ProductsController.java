package com.programandoconjava.presentation.controller;

import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.programandoconjava.application.utils.Constants;
import com.programandoconjava.domain.model.Product;
import com.programandoconjava.domain.model.ProductType;
import com.programandoconjava.domain.service.ProductsService;
import com.programandoconjava.domain.service.PurchasesService;
import com.programandoconjava.presentation.dto.mapping.ProductMapping;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/products")
public class ProductsController {

	private static final Logger logger = LogManager.getLogger(ProductsController.class);

	@Autowired
	private ProductsService productsService;

	@Autowired
	private PurchasesService purchasesService;

	@GetMapping()
	public String getProducts(Model model) {
		
		model.addAttribute(Constants.ATTRIBUTE_NAME_TITLE, Constants.ATTRIBUTE_VALUE_TITLE);
		
		return "products";
	}

	@GetMapping("/templates")
	public String getProductsTemplates(Model model) {
		
		model.addAttribute(Constants.ATTRIBUTE_NAME_TITLE, Constants.ATTRIBUTE_VALUE_TITLE);

		List<Product> products = productsService.getByType(ProductType.WEB_TEMPLATE, true);
		model.addAttribute(Constants.ATTRIBUTE_NAME_WEB_TEMPLATES, ProductMapping.parseListToDTOs(products));
		
		return "products_web_templates";
	}

	@GetMapping("/download/{productId}")
	public String getProductsDownload(Model model, @PathVariable("productId") Long productId, HttpServletRequest servletRequest) {
		logger.info("Downloading product {}", productId);

		model.addAttribute(Constants.ATTRIBUTE_NAME_TITLE, Constants.ATTRIBUTE_VALUE_TITLE);

		Optional<Product> product = productsService.getById(productId, true);

		if (product.isEmpty()) {
			logger.error("Product with id '{}' not found", productId);
			String errorMsg = "El producto que está intentando descargar no existe. <br>" +
								"Si creé que ocurrió un error inesperado, por favor, pongase en contacto con nosotros";
			model.addAttribute(Constants.ATTRIBUTE_NAME_ERROR_MESSAGE, errorMsg);
			return "error";
		}

		model.addAttribute(Constants.ATTRIBUTE_NAME_PRODUCT, ProductMapping.parseToDTO(product.get()));

		boolean allowDownload = purchasesService.validateDownloadToken(productId, servletRequest);

		if (!allowDownload) {
			logger.info("Download of product '{}' NOT allowed", productId);
			String errorMsg = "ERROR! La descarga no es válida. <br>" +
								"Si ha realizado una compra y aún así no puede descargar el producto, por favor, pongase en contacto con nosotros";
			model.addAttribute(Constants.ATTRIBUTE_NAME_ERROR_MESSAGE, errorMsg);
			return "error";
		}
		logger.info("Download of product '{}' allowed", productId);

		return "products_download";
	}

	@GetMapping("/download/file/{productId}")
	public ResponseEntity<?> getProductsDownloadFile(Model model, @PathVariable("productId") Long productId, HttpServletRequest servletRequest) {
		logger.info("Downloading product file {}", productId);

		model.addAttribute(Constants.ATTRIBUTE_NAME_TITLE, Constants.ATTRIBUTE_VALUE_TITLE);

		Optional<Product> product = productsService.getById(productId, true);

		if (product.isEmpty()) {
			logger.error("Product with id '{}' not found", productId);
			return ResponseEntity.notFound().build();
		}

		model.addAttribute(Constants.ATTRIBUTE_NAME_PRODUCT, ProductMapping.parseToDTO(product.get()));

		boolean allowDownload = purchasesService.validateDownloadToken(productId, servletRequest);

		if (!allowDownload) {
			logger.info("Download of product file '{}' NOT allowed", productId);
			return ResponseEntity.status(HttpStatus.FORBIDDEN.value()).build();
		}
		logger.info("Download of product file '{}' allowed", productId);

		String productsPath = "/var/opt/programandoconjava/products/";
		Path filePath = Paths.get(productsPath).resolve(product.get().getFilename()).normalize();
        File file = filePath.toFile();

		try {
			Resource resource = new UrlResource(file.toURI());

			return ResponseEntity.ok()
								.contentType(MediaType.APPLICATION_OCTET_STREAM)
								.header(HttpHeaders.CONTENT_DISPOSITION,
								"attachment; filename=\"" + file.getName() + "\"")
								.body(resource);
		} catch (MalformedURLException e) {
			logger.error("Error creating file URL", e);
			return ResponseEntity.internalServerError().build();
		}
	}
}
