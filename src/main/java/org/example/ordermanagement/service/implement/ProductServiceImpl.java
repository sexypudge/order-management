package org.example.ordermanagement.service.implement;

import jakarta.transaction.Transactional;
import org.example.ordermanagement.common.enums.ProductStatus;
import org.example.ordermanagement.exception.BusinessException;
import org.example.ordermanagement.model.domain.Product;
import org.example.ordermanagement.model.dto.request.CreateProductRequest;
import org.example.ordermanagement.model.dto.response.ProductResponse;
import org.example.ordermanagement.repository.ProductRepository;
import org.example.ordermanagement.service.ProductService;
import org.springframework.stereotype.Service;



@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    @Transactional
    public ProductResponse createProduct(CreateProductRequest request) {
        String sku = normalize(request.getSku());

        if (productRepository.existsBySkuIgnoreCase(sku)) {
            throw new BusinessException("PRODUCT_SKU_ALREADY_EXISTS", "Product SKU already exists");
        }

        Product product = new Product();
        product.setSku(sku);
        product.setName(request.getName().trim());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStockQuantity(request.getStockQuantity());
        product.setLowStockThreshold(request.getLowStockThreshold());
        product.setStatus(ProductStatus.ACTIVE);

        Product saved = productRepository.save(product);
        return toResponse(saved);
    }
    private ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getSku(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStockQuantity(),
                product.getLowStockThreshold(),
                product.getStatus(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }
    private String normalize(String value) {
        return value == null ? null : value.trim();
    }
}
