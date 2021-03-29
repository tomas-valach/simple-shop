package com.valach.simpleshop;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.valach.simpleshop.dao.ProductDao;
import com.valach.simpleshop.dao.impl.OrderDaoImpl;
import com.valach.simpleshop.model.Order;
import com.valach.simpleshop.model.OrderItem;
import com.valach.simpleshop.model.OrderState;
import com.valach.simpleshop.model.ProductItem;
import com.valach.simpleshop.service.impl.CancelOrderServiceImpl;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class CancelOrderServiceImplTest {

  @MockBean
  private ProductDao productDao;
  @MockBean
  private OrderDaoImpl orderDao;

  @Autowired
  private CancelOrderServiceImpl cancelOrderService;

  @Test
  void cancelOrder() {
    Order order = new Order();
    order.setOrderState(OrderState.NEW);
    order.setOrderItems(singletonList(prepareOrderItem()));
    when(orderDao.load(1L)).thenReturn(order);
    ProductItem productItem = prepareProductItem();
    when(productDao.listByIds(singletonList(1L))).thenReturn(singletonList(productItem));

    cancelOrderService.cancelOrder(1L);

    assertEquals(OrderState.CANCELED, order.getOrderState());
    assertEquals(105, productItem.getQuantity());
  }

  @Test
  void cancelPaidOrder() {
    Order order = new Order();
    order.setOrderState(OrderState.PAYED);
    order.setOrderItems(singletonList(prepareOrderItem()));
    when(orderDao.load(1L)).thenReturn(order);
    ProductItem productItem = prepareProductItem();
    when(productDao.listByIds(singletonList(1L))).thenReturn(singletonList(productItem));

    cancelOrderService.cancelOrder(1L);

    assertEquals(OrderState.PAYED, order.getOrderState());
    assertEquals(100, productItem.getQuantity());
  }

  private ProductItem prepareProductItem() {
    ProductItem productItem = new ProductItem();
    productItem.setId(1L);
    productItem.setActive(true);
    productItem.setName("product");
    productItem.setPricePerUnit(BigDecimal.ONE);
    productItem.setQuantity(100);
    return productItem;
  }

  private OrderItem prepareOrderItem() {
    OrderItem orderItem = new OrderItem();
    orderItem.setProductId(1L);
    orderItem.setQuantity(5);
    return orderItem;
  }

}