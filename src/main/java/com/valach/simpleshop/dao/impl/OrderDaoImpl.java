package com.valach.simpleshop.dao.impl;

import com.valach.simpleshop.dao.OrderDao;
import com.valach.simpleshop.model.Order;
import com.valach.simpleshop.service.exception.ShopRuntimeException;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class OrderDaoImpl implements OrderDao {

  @PersistenceContext
  private EntityManager entityManager;

  public Long create(Order order) {
    try {
      entityManager.persist(order);
    } catch (Exception e) {
      throw new ShopRuntimeException("Order persist failed.", e);
    }
    return order.getId();
  }

  public Order load(Long id) {
    Optional<Order> orderInfo;

    try {
      orderInfo = Optional.ofNullable(entityManager.find(Order.class, id));
    } catch (Exception e) {
      throw new ShopRuntimeException("Order load failed.", e);
    }

    if (orderInfo.isEmpty()) {
      throw new ShopRuntimeException("Order with ID %s not found.", id);
    }

    return orderInfo.get();
  }
}
