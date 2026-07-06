package org.example.ordermanagement.controller;


import jakarta.validation.Valid;
import org.example.ordermanagement.model.dto.request.CreateProductRequest;
import org.example.ordermanagement.model.dto.response.ApiResponse;
import org.example.ordermanagement.service.ProductService;
import org.example.ordermanagement.util.ResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<?>> createProduct(@Valid @RequestBody CreateProductRequest request) {
        return ResponseEntity.status(201)
                .body(ResponseUtil.success(productService.createProduct(request.getName())));

    }
}