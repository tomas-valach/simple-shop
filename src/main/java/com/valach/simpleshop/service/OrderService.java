package com.valach.simpleshop.service;

import com.valach.simpleshop.service.pojo.OrderPojo;

public interface OrderService {

  Long createOrder(OrderPojo orderPojo);

  void payOrder(Long id);

  OrderPojo getOrder(Long id);
}
