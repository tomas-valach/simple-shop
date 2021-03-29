package com.valach.simpleshop.dao.impl;

import com.valach.simpleshop.dao.ProductDao;
import com.valach.simpleshop.model.ProductItem;
import com.valach.simpleshop.service.exception.ShopRuntimeException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

@Repository
public class ProductDaoImpl implements ProductDao {

  @PersistenceContext
  private EntityManager entityManager;

  public Long create(ProductItem productItem) {
    try {
      entityManager.persist(productItem);
    } catch (Exception e) {
      throw new ShopRuntimeException("Product persist failed.", e);
    }
    return productItem.getId();
  }

  public ProductItem load(Long id) {
    Optional<ProductItem> productItem;

    try {
      productItem = Optional.ofNullable(entityManager.find(ProductItem.class, id));
    } catch (Exception e) {
      throw new ShopRuntimeException("Product load failed.", e);
    }

    if (productItem.isEmpty()) {
      throw new ShopRuntimeException("Product with ID %s not found.", id);
    }

    return productItem.get();
  }

  public List<ProductItem> list() {
    return listByIds(Collections.emptyList());
  }

  public List<ProductItem> listByIds(List<Long> productIds) {
    try {
      CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
      CriteriaQuery<ProductItem> query = criteriaBuilder.createQuery(ProductItem.class);
      Root<ProductItem> root = query.from(ProductItem.class);

      query.select(root);
      Predicate predicate = criteriaBuilder.equal(root.get("active"), true);

      if (!productIds.isEmpty()) {
        Expression<String> idExpression = root.get("id");
        Predicate idPredicate = idExpression.in(productIds);
        predicate = criteriaBuilder.and(predicate, idPredicate);
      }

      query.where(predicate);

      return entityManager.createQuery(query).getResultList();

    } catch (Exception e) {
      throw new ShopRuntimeException("Products list failed.", e);
    }
  }
}
