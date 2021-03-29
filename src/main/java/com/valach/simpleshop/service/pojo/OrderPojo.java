package com.valach.simpleshop.service.pojo;

import com.valach.simpleshop.model.Order;
import java.util.List;
import java.util.stream.Collectors;

public class OrderPojo {

  private List<OrderItemPojo> items;

  public List<OrderItemPojo> getItems() {
    return items;
  }

  public void setItems(List<OrderItemPojo> items) {
    this.items = items;
  }

  public static OrderPojo mapOrder(Order order) {
    OrderPojo pojo = new OrderPojo();
    pojo.setItems(order.getOrderItems().stream()
        .map(OrderItemPojo::mapOrderItem)
        .collect(Collectors.toList()));

    return pojo;
  }
}
