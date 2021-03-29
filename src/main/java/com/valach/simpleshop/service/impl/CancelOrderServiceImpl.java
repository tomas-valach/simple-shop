package com.valach.simpleshop.service.impl;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import com.valach.simpleshop.dao.OrderDao;
import com.valach.simpleshop.dao.ProductDao;
import com.valach.simpleshop.model.AbstractItem;
import com.valach.simpleshop.model.Order;
import com.valach.simpleshop.model.OrderItem;
import com.valach.simpleshop.model.OrderState;
import com.valach.simpleshop.model.ProductItem;
import com.valach.simpleshop.service.CancelOrderService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CancelOrderServiceImpl implements CancelOrderService {

  private final OrderDao orderDao;
  private final ProductDao productDao;

  @Autowired
  public CancelOrderServiceImpl(OrderDao orderDao, ProductDao productDao) {
    this.orderDao = orderDao;
    this.productDao = productDao;
  }

  @Transactional
  public void cancelOrder(Long id) {
    Order order = orderDao.load(id);

    if (OrderState.PAYED != order.getOrderState()) {
      Map<Long, ProductItem> products = productDao.listByIds(order.getOrderItems().stream()
          .map(OrderItem::getProductId)
          .collect(toList())).stream()
          .collect(toMap(AbstractItem::getId, identity()));

      order.getOrderItems().forEach(i -> products.get(i.getProductId()).increaseQuantityBy(i.getQuantity()));
      order.setOrderState(OrderState.CANCELED);
    }
  }
}
