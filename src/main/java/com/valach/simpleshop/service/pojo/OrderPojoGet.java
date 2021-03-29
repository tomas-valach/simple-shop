package com.valach.simpleshop.service.pojo;

import com.valach.simpleshop.model.Order;
import com.valach.simpleshop.model.OrderState;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class OrderPojoGet {

  private List<OrderItemPojo> items;
  private OrderState orderState;
  private Date created;

  public List<OrderItemPojo> getItems() {
    return items;
  }

  public void setItems(List<OrderItemPojo> items) {
    this.items = items;
  }

  public OrderState getOrderState() {
    return orderState;
  }

  public void setOrderState(OrderState orderState) {
    this.orderState = orderState;
  }

  public Date getCreated() {
    return created;
  }

  public void setCreated(Date created) {
    this.created = created;
  }

  public static OrderPojoGet mapOrder(Order order) {
    OrderPojoGet pojo = new OrderPojoGet();
    pojo.setOrderState(order.getOrderState());
    pojo.setCreated(order.getCreateDateTime());
    pojo.setItems(order.getOrderItems().stream()
        .map(OrderItemPojo::mapOrderItem)
        .collect(Collectors.toList()));

    return pojo;
  }
}
