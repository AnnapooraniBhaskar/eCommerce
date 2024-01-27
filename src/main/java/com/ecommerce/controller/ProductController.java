package com.ecommerce.controller;

import com.ecommerce.entity.DiscountOrTaxRequest;
import com.ecommerce.entity.Product;
import com.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ecommerce")
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/createProduct")
    public ResponseEntity<?> createProduct(@RequestBody Product product) {
        return productService.createProduct(product);
    }

    @GetMapping("/getProduct/{productId}")
    public ResponseEntity<?> getProduct(@PathVariable Long productId) {
        return productService.getProduct(productId);
    }

    @PutMapping("/updateProduct/{productId}")
    public ResponseEntity<?> updateProduct(@PathVariable Long productId, @RequestBody Product product) {
        return productService.updateProduct(productId,product);
    }

    @DeleteMapping("/deleteProduct/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long productId) {
        return productService.deleteProduct(productId);
    }

    @PatchMapping("/applyDiscount/{productId}")
    public ResponseEntity<String> applyDiscount(@PathVariable Long productId, @RequestBody DiscountOrTaxRequest request) {
        return productService.applyDiscountOrTax(productId,request);
    }

//    @PatchMapping("/applyTax/{productId}")
//    public ResponseEntity<?> applyTax(@PathVariable Long productId, @RequestBody DiscountOrTaxRequest request) {
//        return productService.applyDiscountOrTax(productId, request);
//    }

}