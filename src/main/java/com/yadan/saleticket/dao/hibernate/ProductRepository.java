package com.yadan.saleticket.dao.hibernate;

import com.yadan.saleticket.dao.hibernate.base.ExtJpaRepository;
import com.yadan.saleticket.model.product.Product;

public interface ProductRepository extends ExtJpaRepository<Product, Long> {
}
