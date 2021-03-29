package com.valach.simpleshop.service.pojo;

import com.valach.simpleshop.model.ProductItem;
import java.math.BigDecimal;

public class ProductPojo {

  private String name;
  private Integer quantity;
  private BigDecimal pricePerUnit;

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

  public static ProductPojo mapProduct(ProductItem productItem) {
    ProductPojo pojo = new ProductPojo();
    pojo.setName(productItem.getName());
    pojo.setQuantity(productItem.getQuantity());
    pojo.setPricePerUnit(productItem.getPricePerUnit());

    return pojo;
  }

}
