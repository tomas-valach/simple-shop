package com.valach.simpleshop.service.pojo;

import com.valach.simpleshop.model.ProductItem;
import java.math.BigDecimal;

public class ProductListPojo {

  private Long id;
  private String name;
  private Integer quantity;
  private BigDecimal pricePerUnit;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  public BigDecimal getPricePerUnit() {
    return pricePerUnit;
  }

  public void setPricePerUnit(BigDecimal pricePerUnit) {
    this.pricePerUnit = pricePerUnit;
  }

  public static ProductListPojo mapProduct(ProductItem productItem) {
    ProductListPojo pojo = new ProductListPojo();
    pojo.setId(productItem.getId());
    pojo.setName(productItem.getName());
    pojo.setQuantity(productItem.getQuantity());
    pojo.setPricePerUnit(productItem.getPricePerUnit());

    return pojo;
  }

}
