package com.valach.simpleshop.controller;

import com.valach.simpleshop.service.ProductService;
import com.valach.simpleshop.service.pojo.ProductListPojo;
import com.valach.simpleshop.service.pojo.ProductPojo;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/product")
public class ProductController {

  private final ProductService productService;

  @Autowired
  public ProductController(ProductService productService) {
    this.productService = productService;
  }

  @PostMapping(value = "/create")
  public ResponseEntity<Long> createProduct(@RequestBody ProductPojo productPojo) {
    Long productId = productService.createProduct(productPojo);
    return ResponseEntity.ok(productId);
  }

  @GetMapping(value = "/list")
  public ResponseEntity<List<ProductListPojo>> listProducts() {
    List<ProductListPojo> products = productService.list();
    return ResponseEntity.ok(products);
  }

  @PostMapping("/update/{id}")
  public ResponseEntity<Object> updateProduct(@PathVariable Long id, @RequestBody ProductPojo productPojo) {
    productService.updateProduct(id, productPojo);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/deactivate/{id}")
  public ResponseEntity<Object> deactivateProduct(@PathVariable Long id) {
    productService.deactivateProduct(id);
    return ResponseEntity.ok().build();
  }
}
