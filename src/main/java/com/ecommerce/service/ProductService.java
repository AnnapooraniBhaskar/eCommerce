package com.ecommerce.service;

import com.ecommerce.entity.DiscountOrTaxRequest;
import com.ecommerce.entity.Product;
import com.ecommerce.productRepository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductService {


    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ResponseEntity<?> createProduct(Product product) {
        try {
            Product savedProduct = productRepository.save(product);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to create product");
        }
    }
    public ResponseEntity<?> updateProduct(Long productId, Product product) {
        if (productRepository.existsById(productId)) {
            product.setProductId(productId);
            productRepository.save(product);
            return ResponseEntity.status(HttpStatus.OK).body("Product updated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        }
    }

    public ResponseEntity<?> getProduct(Long productId) {
        if (productRepository.existsById(productId)) {
            Optional<Product> product = productRepository.findById(productId);
            return product.map(value -> ResponseEntity.status(HttpStatus.OK).body(value))
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        }
    }

    public ResponseEntity<?> deleteProduct(Long productId) {
        if (productRepository.existsById(productId)) {
            productRepository.deleteById(productId);
            return ResponseEntity.status(HttpStatus.OK).body("Product deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        }
    }

        public ResponseEntity<String > applyDiscountOrTax(Long productId, DiscountOrTaxRequest request) {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            if ("discount".equals(request.getType())) {
                double discountedPrice = product.getPrice() - (product.getPrice() * request.getPercentage() / 100);
                product.setPrice(discountedPrice);
            } else if ("tax".equals(request.getType())) {
                double taxedPrice = product.getPrice() + (product.getPrice() * request.getPercentage() / 100);
                product.setPrice(taxedPrice);
            }
            productRepository.save(product);
            return ResponseEntity.ok("Success: " + request.getType() + " applied. Updated Product details: " + product);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        }
    }
}
