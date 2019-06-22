package com.yadan.saleticket.service;

import com.yadan.saleticket.entity.AddOrderVo;
import com.yadan.saleticket.model.order.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminOrderService {
    @Autowired
    private OnlineOrderGenerator onlineOrderGenerator;
    @Autowired
    private OfflineOrderGenerator offlineOrderGenerator;

    /**
     * 创建订单
     * @param addOrderVo
     * @return
     */
    public Order createOrder(AddOrderVo addOrderVo) {
        if (addOrderVo.getOnlineSale()) {
            return onlineOrderGenerator.createOrder(addOrderVo);
        }else {
            return offlineOrderGenerator.createOrder(addOrderVo);
        }
    }
}
