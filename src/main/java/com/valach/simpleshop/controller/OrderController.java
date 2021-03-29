package com.valach.simpleshop.controller;

import com.valach.simpleshop.service.CancelOrderService;
import com.valach.simpleshop.service.OrderService;
import com.valach.simpleshop.service.impl.OrderServiceImpl;
import com.valach.simpleshop.service.pojo.OrderPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order")
public class OrderController {

  private final OrderService orderService;
  private final CancelOrderService cancelOrder;

  @Autowired
  public OrderController(OrderServiceImpl orderServiceImpl, CancelOrderService cancelOrder) {
    this.orderService = orderServiceImpl;
    this.cancelOrder = cancelOrder;
  }

  @PostMapping("/create")
  public ResponseEntity<Long> createOrder(@RequestBody OrderPojo orderPojo) {
    Long orderId = orderService.createOrder(orderPojo);
    return ResponseEntity.ok(orderId);
  }

  @GetMapping("/{id}")
  public ResponseEntity<OrderPojo> getOrder(@PathVariable Long id) {
    OrderPojo orders = orderService.getOrder(id);
    return ResponseEntity.ok(orders);
  }

  @PostMapping("/pay/{id}")
  public ResponseEntity<Object> payOrder(@PathVariable Long id) {
    orderService.payOrder(id);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/cancel/{id}")
  public ResponseEntity<Object> cancelOrder(@PathVariable Long id) {
    cancelOrder.cancelOrder(id);
    return ResponseEntity.ok().build();
  }
}
