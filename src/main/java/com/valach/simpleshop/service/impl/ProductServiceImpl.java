package com.valach.simpleshop.service.impl;

import static java.util.stream.Collectors.toList;

import com.valach.simpleshop.dao.ProductDao;
import com.valach.simpleshop.model.ProductItem;
import com.valach.simpleshop.service.ProductService;
import com.valach.simpleshop.service.exception.ShopRuntimeException;
import com.valach.simpleshop.service.pojo.ProductListPojo;
import com.valach.simpleshop.service.pojo.ProductPojo;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductServiceImpl implements ProductService {

  private final ProductDao productDao;

  @Autowired
  public ProductServiceImpl(ProductDao productDao) {
    this.productDao = productDao;
  }

  @Transactional
  public Long createProduct(ProductPojo productPojo) {
    ProductItem productItem = new ProductItem();
    productItem.setQuantity(productPojo.getQuantity());
    productItem.setPricePerUnit(productPojo.getPricePerUnit());
    productItem.setName(productPojo.getName());
    productItem.setActive(true);

    return productDao.create(productItem);
  }

  @Transactional
  public void deactivateProduct(Long id) {
    ProductItem productItem = productDao.load(id);
    productItem.setActive(false);
    productItem.setQuantity(0);
  }

  @Transactional
  public void updateProduct(Long id, ProductPojo productPojo) {
    ProductItem productItem = productDao.load(id);

    if (Boolean.TRUE.equals(productItem.getActive())) {
      productItem.setName(productPojo.getName());
      productItem.setPricePerUnit(productPojo.getPricePerUnit());
      productItem.setQuantity(productPojo.getQuantity());
    } else {
      throw new ShopRuntimeException("Inactive product with ID %s cannot be updated.", id);
    }
  }

  @Transactional(readOnly = true)
  public List<ProductListPojo> list() {
    return productDao.list().stream()
        .map(ProductListPojo::mapProduct)
        .collect(toList());
  }
}
