package com.ecommerce.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import com.ecommerce.entity.DiscountOrTaxRequest;
import com.ecommerce.entity.Product;
import com.ecommerce.productRepository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ProductServiceTest {

    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    public void setup() {
        productRepository = mock(ProductRepository.class);
        productService = new ProductService(productRepository);
    }


    @Test
    public void testCreateProduct_Success() {
        Product product = buildProduct();
        when(productRepository.save(product)).thenReturn(product);

        ResponseEntity<?> response = productService.createProduct(product);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(product, response.getBody());
    }

    @Test
    public void testUpdateProduct_Success() {

        Long productId = 1001L;
        Product product = buildProduct();
        when(productRepository.existsById(productId)).thenReturn(true);
        when(productRepository.save(product)).thenReturn(product);

        ResponseEntity<?> response = productService.updateProduct(productId, product);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Product updated successfully", response.getBody());
    }

    @Test
    public void testGetProduct_NotFound() {
        Long productId = 1001L;
        when(productRepository.existsById(productId)).thenReturn(false);

        ResponseEntity<?> response = productService.getProduct(productId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Product not found", response.getBody());
    }

    @Test
    public void testApplyDiscountOrTax_Discount_Success() {
        Long productId = 1001L;
        DiscountOrTaxRequest request = buildDiscountTaxRequest("Discount",10.0);
        Product product = buildProduct();
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        ResponseEntity<?> response = productService.applyDiscountOrTax(productId, request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testApplyDiscountOrTax_Tax_Success() {
        Long productId = 1001L;
        DiscountOrTaxRequest request = buildDiscountTaxRequest("Tax",20.0);
        Product product = buildProduct();
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        ResponseEntity<?> response = productService.applyDiscountOrTax(productId, request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    private DiscountOrTaxRequest buildDiscountTaxRequest(String type,double percentage) {
     return DiscountOrTaxRequest.builder().
             type(type)
             .percentage(percentage)
             .build();
    }
    private Product buildProduct() {
        return new Product(1001L, "Test Product", "Test Description", 1000.0, 100);
    }

}
