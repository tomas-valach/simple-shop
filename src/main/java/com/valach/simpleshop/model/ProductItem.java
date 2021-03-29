package com.valach.simpleshop.model;

import com.valach.simpleshop.service.exception.ShopRuntimeException;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Entity
public class ProductItem extends AbstractItem {

  @NotNull
  private Boolean active;

  public Boolean getActive() {
    return active;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }

  public void decreaseQuantityBy(int value) {
    if (!hasEnoughQuantity(value)) {
      throw new ShopRuntimeException("Insufficient quantity of product with ID %s.", id);
    }
    quantity -= value;
  }

  public void increaseQuantityBy(int value) {
    quantity += value;
  }

  public boolean hasEnoughQuantity(int desiredQuantity) {
    return quantity >= desiredQuantity;
  }

}
