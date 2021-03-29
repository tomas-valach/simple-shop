package com.valach.simpleshop.service;

import com.valach.simpleshop.service.pojo.OrderPojo;
import com.valach.simpleshop.service.pojo.OrderPojoGet;

public interface OrderService {

  Long createOrder(OrderPojo orderPojo);

  void payOrder(Long id);

  OrderPojoGet getOrder(Long id);
}
