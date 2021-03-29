package com.valach.simpleshop.service.pojo;

import java.util.List;

public class OrderPojo {

  private List<OrderItemPojo> items;

  public List<OrderItemPojo> getItems() {
    return items;
  }

  public void setItems(List<OrderItemPojo> items) {
    this.items = items;
  }
}
