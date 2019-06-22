package com.yadan.saleticket.service;

import com.yadan.saleticket.dao.redis.RedisHelper;
import com.yadan.saleticket.dao.redis.RedisKeyPrefix;
import com.yadan.saleticket.entity.ProductSeatCacheVo;
import com.yadan.saleticket.model.product.Product;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ProductRedisService {

    @Autowired
    private RedisHelper redisHelper;

    public void createProductCache(Product product) {
        if (!product.getOnlineSale() || CollectionUtils.isEmpty(product.getProductDetails())) {
            return;
        }
        product.getProductDetails().stream().forEach(each -> {

            String key = this.getKey(each.getId());
            Object old = redisHelper.readValue(key, HashMap.class);
            if (old != null) {
                redisHelper.delete(key);
            }

            Map newValue = new HashMap<Long, ProductSeatCacheVo>();
            each.getProductPrices().stream().forEach(pp -> {
                pp.getSeats().stream().forEach(seat -> {
                    ProductSeatCacheVo cacheVo = new ProductSeatCacheVo();
                    cacheVo.setSeatId(seat.getId());
                    cacheVo.setProductPriceId(pp.getId());
                    cacheVo.setSell(false);
                    cacheVo.setPrice(pp.getPrice());
                    cacheVo.setSeatColumn(seat.getSeatColumn());
                    cacheVo.setSeatRow(seat.getSeatRow());
                    cacheVo.setSiteColumn(seat.getSiteColumn());
                    cacheVo.setSiteRow(seat.getSiteRow());
                    newValue.put(seat.getId(), cacheVo);
                });
            });
            redisHelper.writeValue(key, newValue);
        });
    }

    public void removeProductCache(Product product) {
        if (!product.getOnlineSale() || CollectionUtils.isEmpty(product.getProductDetails())) {
            return;
        }
        product.getProductDetails().stream().forEach(each -> {
            redisHelper.delete(this.getKey(each.getId()));
        });

    }

    public void updateProductCache() {


    }

    public void getProductCache() {
    }

    private String getKey(Long productDetailId) {
        return String.format("%s-%s", RedisKeyPrefix.PRODUCT_DETAIL_KEY.getPrefix(), productDetailId);

    }

}
