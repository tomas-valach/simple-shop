package com.valach.simpleshop.dao;

import com.valach.simpleshop.model.Order;

public interface OrderDao {

  Long create(Order order);

  Order load(Long id);
}