package com.yadan.saleticket.service.order;


import com.yadan.saleticket.base.exception.ExceptionCode;
import com.yadan.saleticket.base.exception.ServiceException;
import com.yadan.saleticket.base.security.SecurityService;
import com.yadan.saleticket.base.tools.CommonUtils;
import com.yadan.saleticket.base.tools.DateUtils;
import com.yadan.saleticket.dao.hibernate.*;
import com.yadan.saleticket.entity.order.AddOrderDetailVo;
import com.yadan.saleticket.entity.order.AddOrderVo;
import com.yadan.saleticket.model.order.Order;
import com.yadan.saleticket.model.order.Ticket;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public abstract class AbstractOrderGenerator {

    @Autowired
    protected ProductPriceRepository productPriceRepository;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected OrderRepository orderRepository;

    @Autowired
    protected SecurityService securityService;

    @Autowired
    protected TicketRepository ticketRepository;

    @Autowired
    protected SeatRepository seatRepository;

    /**
     * 创建完整的订单
     *
     * @param addOrderVo
     * @return
     */
    @Transactional
    public Order createOrder(AddOrderVo addOrderVo) {
        // 参数校验
        this.createOrderCheckParam(addOrderVo);

        // 库存校验
        this.checkInventory(addOrderVo.getAddOrderDetailVos());

        // 创建订单
        Order savedOrder = this.createSavedOrder(addOrderVo);

        // 生成实体票
        for (AddOrderDetailVo addOrderDetailVo : addOrderVo.getAddOrderDetailVos()) {
            this.createSavedTicket(savedOrder, addOrderDetailVo);
        }

        // todo 更新redis

        return savedOrder;
    }

    /**
     * 下单参数校验
     *
     * @param addOrderVo
     */
    protected void createOrderCheckParam(AddOrderVo addOrderVo) {
        if (addOrderVo.getOnlineSale() == null) {
            throw new ServiceException(ExceptionCode.INVALID_CREATE_ORDER, "下单getOnlineSale不能为空");
        }

        if (addOrderVo.getUserId() == null) {
            throw new ServiceException(ExceptionCode.INVALID_CREATE_ORDER, "下单用户不能为空");
        }

        if (CollectionUtils.isEmpty(addOrderVo.getAddOrderDetailVos())) {
            throw new ServiceException(ExceptionCode.INVALID_CREATE_ORDER, "下单票务信息不能为空");
        }
    }

    /**
     * 库存校验
     */
    protected abstract void checkInventory(List<AddOrderDetailVo> addOrderDetailVos);

    /**
     * 创建实体订单
     *
     * @param addOrderVo
     * @return
     */
    protected Order createSavedOrder(AddOrderVo addOrderVo) {
        Order order = new Order();
        String orderNo = DateUtils.format(LocalDateTime.now(), DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + CommonUtils.genRandomNumCode(4);
        order.setOrderNo(orderNo);
        BigDecimal price = addOrderVo.getAddOrderDetailVos().stream().map(each -> productPriceRepository.getOne(each.getProductPriceId()).getPrice())
                .reduce(BigDecimal::add).get();
        order.setOriginPrice(price);
        order.setPrice(price);
        order.setUser(userRepository.getOne(addOrderVo.getUserId()));
        order.setCreater(securityService.getCurrentLoginUser());
        order.setQuantity(addOrderVo.getAddOrderDetailVos().size());
        this.defineOrder(order);
        orderRepository.save(order);

        return order;
    }

    /**
     * 自定义订单属性
     *
     * @param order
     * @return
     */
    protected abstract Order defineOrder(Order order);

    /**
     * 创建实体票
     *
     * @param savedOrder
     * @param addOrderDetailVo
     * @return
     */
    protected abstract Ticket createSavedTicket(Order savedOrder, AddOrderDetailVo addOrderDetailVo);

    protected String genTicketNumber(String productPriceNumber, String siteRow, String siteColumn) {
        return productPriceNumber + StringUtils.leftPad(siteRow, 3, "0") + StringUtils.leftPad(siteColumn, 3, "0");
    }

//    public abstract void refundOrder(Long orderId);
//
//    public abstract void payOrder(Long orderId);

}
