package com.valach.simpleshop.dao;

import com.valach.simpleshop.model.ProductItem;
import java.util.List;

public interface ProductDao {

  Long create(ProductItem productItem);

  ProductItem load(Long id);

  List<ProductItem> list();

  List<ProductItem> listByIds(List<Long> productIds);
}