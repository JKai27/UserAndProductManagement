package com.marcapo.template.controllers;

import com.marcapo.template.documents.Product;
import com.marcapo.template.dto.CreateProductRequest;
import com.marcapo.template.exceptions.PriceCanNotBeLessThanZero;
import com.marcapo.template.repository.ProductRepository;
import com.marcapo.template.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    ProductService productService;
    ProductRepository productRepository;

    @Autowired
    public ProductController(ProductService productService, ProductRepository productRepository) {
        this.productService = productService;
        this.productRepository = productRepository;
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody List<CreateProductRequest> request) throws PriceCanNotBeLessThanZero {
        productService.createProducts(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(request);
    }

    @GetMapping("/{name}")
    public Product findProductByName(@PathVariable String name) throws Exception {
        return productService.findByName(name);
    }

    @GetMapping("/search/by-date")
    public ResponseEntity<List<Product>> findProductsBetweenDates(
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate) {

        if (!startDate.matches("\\d{4}-\\d{2}-\\d{2}") || !endDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }
        List<Product> products = productRepository.findByManufacturingDateBetween(startDate, endDate);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/search/by-price")
    public ResponseEntity<List<Product>> findProductsByPriceBetween(@RequestParam double price1, @RequestParam double price2) throws PriceCanNotBeLessThanZero {
        List<Product> products = productService.findByPriceBetween(price1, price2);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/all")
    List<Product> getAllProducts() {
        productRepository.findAll();
        return new ArrayList<>(productRepository.findAll());
    }

    @DeleteMapping("/deleteAll")
    public ResponseEntity<?> deleteAllProducts() {
        productRepository.deleteAll();
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("delete/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable String productId) {
        productRepository.deleteById(productId);
        return ResponseEntity.ok("The product with " + productId + " was deleted.");
    }
}
