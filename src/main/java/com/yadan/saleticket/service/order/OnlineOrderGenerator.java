package com.yadan.saleticket.service.order;

import com.yadan.saleticket.base.exception.ExceptionCode;
import com.yadan.saleticket.base.exception.ServiceException;
import com.yadan.saleticket.entity.order.AddOrderDetailVo;
import com.yadan.saleticket.entity.order.AddOrderVo;
import com.yadan.saleticket.enums.OrderStatusEnum;
import com.yadan.saleticket.model.order.Order;
import com.yadan.saleticket.model.order.Ticket;
import com.yadan.saleticket.model.product.ProductDetail;
import com.yadan.saleticket.model.product.ProductPrice;
import com.yadan.saleticket.model.theatre.Seat;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OnlineOrderGenerator extends AbstractOrderGenerator {

    @Override
    protected void createOrderCheckParam(AddOrderVo addOrderVo) {
        super.createOrderCheckParam(addOrderVo);
        for (AddOrderDetailVo addOrderDetailVo : addOrderVo.getAddOrderDetailVos()) {
            if (addOrderDetailVo.getSeatId() == null) {
                throw new ServiceException(ExceptionCode.INVALID_CREATE_ORDER, "下单座位不能为空");
            }
        }
    }

    @Override
    protected void checkInventory(List<AddOrderDetailVo> addOrderDetailVos) {
        // todo 从redis中校验库存
    }

    @Override
    protected Order defineOrder(Order order) {
        order.setOrderStatusEnum(OrderStatusEnum.WAITING_FOR_PAY);
        return order;
    }

    @Override
    protected Ticket createSavedTicket(Order savedOrder, AddOrderDetailVo addOrderDetailVo) {
        ProductPrice pp = productPriceRepository.findOne(addOrderDetailVo.getProductPriceId());
        ProductDetail pd = pp.getProductDetail();
        Seat seat = seatRepository.findOne(addOrderDetailVo.getSeatId());
        Ticket ticket = new Ticket();
        ticket.setOrder(savedOrder);
        ticket.setStartTime(pd.getStartTime());
        ticket.setEndTime(pd.getEndTime());
        ticket.setNumber(genTicketNumber(pd.getNumber(), seat.getSiteRow().toString(), seat.getSiteColumn().toString()));
        ticket.setProductPrice(pp);
        ticket.setSeatRow(seat.getSiteRow());
        ticket.setSeatColumn(seat.getSiteColumn());

        return ticket;
    }
}
