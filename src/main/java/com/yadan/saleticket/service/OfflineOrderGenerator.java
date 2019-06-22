package com.yadan.saleticket.service;

import com.yadan.saleticket.entity.AddOrderDetailVo;
import com.yadan.saleticket.model.order.Order;
import com.yadan.saleticket.model.order.Ticket;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OfflineOrderGenerator extends AbstractOrderGenerator {
    @Override
    protected void checkInventory(List<AddOrderDetailVo> addOrderDetailVos) {

    }

    @Override
    protected Order defineOrder(Order order) {
        return null;
    }

    @Override
    protected Ticket createSavedTicket(Order savedOrder, AddOrderDetailVo addOrderDetailVo) {
        return null;
    }
}
