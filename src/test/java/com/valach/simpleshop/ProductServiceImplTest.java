package com.valach.simpleshop;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.valach.simpleshop.dao.ProductDao;
import com.valach.simpleshop.model.ProductItem;
import com.valach.simpleshop.service.exception.ShopRuntimeException;
import com.valach.simpleshop.service.impl.ProductServiceImpl;
import com.valach.simpleshop.service.pojo.ProductListPojo;
import com.valach.simpleshop.service.pojo.ProductPojo;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class ProductServiceImplTest {

  private static final long PRODUCT_ID = 1L;

  @MockBean
  private ProductDao productDao;

  @Autowired
  private ProductServiceImpl productService;

  @Test
  void createProduct() {
    ProductPojo productPojo = prepareProductPojo();

    productService.createProduct(productPojo);

    ArgumentCaptor<ProductItem> captor = ArgumentCaptor.forClass(ProductItem.class);
    verify(productDao).create(captor.capture());
    assertNotNull(captor.getValue());
    assertTrue(captor.getValue().getActive());
    assertEquals(productPojo.getName(), captor.getValue().getName());
    assertEquals(productPojo.getQuantity(), captor.getValue().getQuantity());
    assertEquals(productPojo.getPricePerUnit(), captor.getValue().getPricePerUnit());
  }

  @Test
  void deactivateProduct() {
    ProductItem productItem = new ProductItem();
    productItem.setActive(true);
    when(productDao.load(PRODUCT_ID)).thenReturn(productItem);

    productService.deactivateProduct(PRODUCT_ID);

    assertNotNull(productItem);
    assertFalse(productItem.getActive());
    assertEquals(0, productItem.getQuantity());
  }

  @Test
  void updateProduct() {
    ProductItem productItem = prepareProductItem(PRODUCT_ID);
    when(productDao.load(PRODUCT_ID)).thenReturn(productItem);
    ProductPojo productPojo = prepareProductPojo();

    productService.updateProduct(PRODUCT_ID, productPojo);

    assertNotNull(productItem);
    assertEquals(PRODUCT_ID, productItem.getId());
    assertEquals(true, productItem.getActive());
    assertEquals(productPojo.getName(), productItem.getName());
    assertEquals(productPojo.getQuantity(), productItem.getQuantity());
    assertEquals(productPojo.getPricePerUnit(), productItem.getPricePerUnit());
  }

  @Test
  void updateInactiveProduct() {
    ProductItem productItem = prepareProductItem(PRODUCT_ID);
    productItem.setActive(false);
    when(productDao.load(PRODUCT_ID)).thenReturn(productItem);
    ProductPojo productPojo = prepareProductPojo();

    ShopRuntimeException exception = assertThrows(ShopRuntimeException.class,
        () -> productService.updateProduct(PRODUCT_ID, productPojo));

    assertEquals(String.format("Inactive product with ID %s cannot be updated.", PRODUCT_ID), exception.getMessage());
  }

  @Test
  void listProducts() {
    when(productDao.list()).thenReturn(asList(
        prepareProductItem(PRODUCT_ID),
        prepareProductItem(2L)
    ));

    List<ProductListPojo> products = productService.list();

    assertNotNull(products);
    assertEquals(2, products.size());
    products.forEach(p -> {
      assertNotNull(p.getId());
      assertEquals("oldName", p.getName());
      assertEquals(8, p.getQuantity(), 8);
      assertEquals(BigDecimal.ONE, p.getPricePerUnit());
    });
  }

  private ProductItem prepareProductItem(long id) {
    ProductItem productItem = new ProductItem();
    productItem.setId(id);
    productItem.setActive(true);
    productItem.setName("oldName");
    productItem.setQuantity(10);
    productItem.setPricePerUnit(BigDecimal.ONE);
    return productItem;
  }

  private ProductPojo prepareProductPojo() {
    ProductPojo productPojo = new ProductPojo();
    productPojo.setName("newName");
    productPojo.setQuantity(8);
    productPojo.setPricePerUnit(BigDecimal.TEN);
    return productPojo;
  }

}