package com.valach.simpleshop.service.pojo;

import com.valach.simpleshop.model.OrderItem;

public class OrderItemPojo {

  private Long productId;
  private Integer quantity;

  public Long getProductId() {
    return productId;
  }

  public void setProductId(Long productId) {
    this.productId = productId;
  }

  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  public static OrderItemPojo mapOrderItem(OrderItem orderItem) {
    OrderItemPojo pojo = new OrderItemPojo();
    pojo.setProductId(orderItem.getId());
    pojo.setQuantity(orderItem.getQuantity());

    return pojo;
  }
}
