package com.valach.simpleshop.service.impl;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import com.valach.simpleshop.dao.OrderDao;
import com.valach.simpleshop.dao.ProductDao;
import com.valach.simpleshop.dao.impl.OrderDaoImpl;
import com.valach.simpleshop.model.AbstractItem;
import com.valach.simpleshop.model.Order;
import com.valach.simpleshop.model.OrderItem;
import com.valach.simpleshop.model.OrderState;
import com.valach.simpleshop.model.ProductItem;
import com.valach.simpleshop.service.CancelOrderService;
import com.valach.simpleshop.service.OrderService;
import com.valach.simpleshop.service.exception.ShopRuntimeException;
import com.valach.simpleshop.service.pojo.OrderItemPojo;
import com.valach.simpleshop.service.pojo.OrderPojo;
import com.valach.simpleshop.service.pojo.OrderPojoGet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderServiceImpl implements OrderService {

  @Value("${order.cancel.timeoutInMinutes}")
  private int orderCancelTimeout;

  private final OrderDao orderDao;
  private final ProductDao productDao;
  private final CancelOrderService cancelOrderService;

  @Autowired
  public OrderServiceImpl(OrderDaoImpl orderDao, ProductDao productDao, CancelOrderServiceImpl cancelOrderService) {
    this.orderDao = orderDao;
    this.productDao = productDao;
    this.cancelOrderService = cancelOrderService;
  }

  @Transactional
  public Long createOrder(OrderPojo orderPojo) {
    List<Long> orderedProductIds = orderPojo.getItems().stream()
        .map(OrderItemPojo::getProductId)
        .collect(toList());
    List<ProductItem> products = productDao.listByIds(orderedProductIds);

    checkMissingProducts(orderedProductIds, products);
    Map<Long, ProductItem> productsMap = products.stream()
        .collect(toMap(AbstractItem::getId, identity()));
    checkOrderValidation(orderPojo.getItems(), productsMap);

    Order order = prepareOrder(orderPojo.getItems(), productsMap);
    Long orderId = orderDao.create(order);

    scheduleCancelUnpaidOrder(orderId);

    return orderId;
  }

  private void checkMissingProducts(List<Long> orderedProductIds, List<ProductItem> products) {
    if (products.size() != orderedProductIds.size()) {
      List<Long> productIds = products.stream()
          .map(ProductItem::getId)
          .collect(toList());

      List<String> missingProductsIds = orderedProductIds.stream()
          .filter(op -> !productIds.contains(op))
          .map(String::valueOf)
          .collect(toList());

      throw new ShopRuntimeException("Products with IDs [%s] are not in stock.",
          String.join(", ", missingProductsIds));
    }
  }

  private void checkOrderValidation(List<OrderItemPojo> orderedItems, Map<Long, ProductItem> productsMap) {
    List<String> invalidProductIds = new ArrayList<>();

    for (OrderItemPojo orderItem : orderedItems) {
      ProductItem productItem = productsMap.get(orderItem.getProductId());
      if (!productItem.hasEnoughQuantity(orderItem.getQuantity())) {
        invalidProductIds.add(String.valueOf(productItem.getId()));
      }
    }
    if (!invalidProductIds.isEmpty()) {
      throw new ShopRuntimeException("Products with IDs [%s] do not have enough quantity in the stock.",
          String.join(", ", invalidProductIds));
    }
  }

  private Order prepareOrder(List<OrderItemPojo> orderedItems, Map<Long, ProductItem> productsMap) {
    Order order = new Order();
    order.setCreateDateTime(new Date());
    order.setOrderState(OrderState.NEW);
    order.setOrderItems(orderedItems.stream()
        .map(i -> prepareOrderItem(order, productsMap.get(i.getProductId()), i))
        .collect(toList()));

    return order;
  }

  private OrderItem prepareOrderItem(Order order, ProductItem product, OrderItemPojo item) {
    product.decreaseQuantityBy(item.getQuantity());

    OrderItem orderItem = new OrderItem();
    orderItem.setQuantity(item.getQuantity());
    orderItem.setPricePerUnit(product.getPricePerUnit());
    orderItem.setName(product.getName());
    orderItem.setProductId(product.getId());
    orderItem.setOrder(order);

    return orderItem;
  }

  private void scheduleCancelUnpaidOrder(Long orderId) {
    ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    executorService.schedule(() -> cancelOrderService.cancelOrder(orderId), orderCancelTimeout, TimeUnit.MINUTES);
  }

  @Transactional
  public void payOrder(Long id) {
    Order order = orderDao.load(id);

    if (OrderState.CANCELED == order.getOrderState()) {
      throw new ShopRuntimeException("Cancel order with ID %s cannot be payed.", id);
    }

    order.setOrderState(OrderState.PAYED);
  }

  @Transactional(readOnly = true)
  public OrderPojoGet getOrder(Long id) {
    Order order = orderDao.load(id);
    return OrderPojoGet.mapOrder(order);
  }

}
