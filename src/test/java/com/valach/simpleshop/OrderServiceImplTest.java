package com.valach.simpleshop;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.valach.simpleshop.dao.ProductDao;
import com.valach.simpleshop.dao.impl.OrderDaoImpl;
import com.valach.simpleshop.model.Order;
import com.valach.simpleshop.model.OrderItem;
import com.valach.simpleshop.model.OrderState;
import com.valach.simpleshop.model.ProductItem;
import com.valach.simpleshop.service.exception.ShopRuntimeException;
import com.valach.simpleshop.service.impl.OrderServiceImpl;
import com.valach.simpleshop.service.pojo.OrderItemPojo;
import com.valach.simpleshop.service.pojo.OrderPojo;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class OrderServiceImplTest {

  @MockBean
  private ProductDao productDao;
  @MockBean
  private OrderDaoImpl orderDao;

  @Autowired
  private OrderServiceImpl orderService;

  @Test
  void createOrder() {
    OrderItemPojo orderItemPojo = prepareOrderItemPojo();
    OrderPojo orderPojo = new OrderPojo();
    orderPojo.setItems(singletonList(orderItemPojo));
    ProductItem productItem = prepareProductItem();
    when(productDao.listByIds(singletonList(1L))).thenReturn(singletonList(productItem));

    orderService.createOrder(orderPojo);

    ArgumentCaptor<Order> captor = ArgumentCaptor.forClass(Order.class);
    verify(orderDao).create(captor.capture());
    Order capturedOrder = captor.getValue();
    assertNotNull(capturedOrder);
    assertEquals(OrderState.NEW, capturedOrder.getOrderState());
    assertFalse(capturedOrder.getOrderItems().isEmpty());
    OrderItem capturedOrderItem = capturedOrder.getOrderItems().get(0);
    assertEquals(productItem.getId(), capturedOrderItem.getProductId());
    assertEquals(productItem.getName(), capturedOrderItem.getName());
    assertEquals(productItem.getPricePerUnit(), capturedOrderItem.getPricePerUnit());
    assertEquals(orderItemPojo.getQuantity(), capturedOrderItem.getQuantity());
    assertEquals(95, productItem.getQuantity());
  }

  @Test
  void createOrder_notEnoughQuantity() {
    OrderItemPojo orderItemPojo = prepareOrderItemPojo();
    orderItemPojo.setQuantity(500);
    OrderPojo orderPojo = new OrderPojo();
    orderPojo.setItems(singletonList(orderItemPojo));
    ProductItem productItem = prepareProductItem();
    when(productDao.listByIds(singletonList(1L))).thenReturn(singletonList(productItem));

    ShopRuntimeException exception = assertThrows(ShopRuntimeException.class, () -> orderService.createOrder(orderPojo));
    assertEquals("Products with IDs [1] do not have enough quantity in the stock.", exception.getMessage());
  }

  @Test
  void createOrder_productNotInStock() {
    OrderItemPojo orderItemPojo = prepareOrderItemPojo();
    orderItemPojo.setProductId(33L);
    OrderPojo orderPojo = new OrderPojo();
    orderPojo.setItems(singletonList(orderItemPojo));
    ProductItem productItem = prepareProductItem();
    when(productDao.listByIds(singletonList(1L))).thenReturn(singletonList(productItem));

    ShopRuntimeException exception = assertThrows(ShopRuntimeException.class, () -> orderService.createOrder(orderPojo));
    assertEquals("Products with IDs [33] are not in stock.", exception.getMessage());
  }

  private OrderItemPojo prepareOrderItemPojo() {
    OrderItemPojo orderItemPojo = new OrderItemPojo();
    orderItemPojo.setProductId(1L);
    orderItemPojo.setQuantity(5);
    return orderItemPojo;
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

}