package com.valach.simpleshop.service;

import com.valach.simpleshop.service.pojo.ProductListPojo;
import com.valach.simpleshop.service.pojo.ProductPojo;
import java.util.List;

public interface ProductService {

  Long createProduct(ProductPojo productPojo);

  void deactivateProduct(Long id);

  void updateProduct(Long id, ProductPojo productPojo);

  List<ProductListPojo> list();
}
