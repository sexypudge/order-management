package org.example.ordermanagement.service;

import org.example.ordermanagement.model.dto.request.CreateProductRequest;
import org.example.ordermanagement.model.dto.response.OrderResponse;

public interface ProductService {
    OrderResponse createProduct(CreateProductRequest request);

}