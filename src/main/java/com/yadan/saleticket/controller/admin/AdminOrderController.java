package com.yadan.saleticket.controller.admin;

import com.yadan.saleticket.base.tools.BeanUtils;
import com.yadan.saleticket.dao.hibernate.OrderRepository;
import com.yadan.saleticket.dao.hibernate.base.STPageRequest;
import com.yadan.saleticket.entity.AddUserVo;
import com.yadan.saleticket.entity.PageVo;
import com.yadan.saleticket.entity.order.AddOrderVo;
import com.yadan.saleticket.model.order.Order;
import com.yadan.saleticket.model.user.User;
import com.yadan.saleticket.service.order.AdminOrderService;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/admin/order")
public class AdminOrderController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private AdminOrderService adminOrderService;

    @GetMapping("")
    public PageVo<Order> orders(STPageRequest pageRequest) {
        PageVo<Order> orders = orderRepository.findAllByFilterAndPageRequest(pageRequest);
        return orders;
    }

    @GetMapping("/{id}")
    public Order order(@PathVariable("id") Long id) {
        return orderRepository.findOne(id);
    }

    @PostMapping("/merge")
    public User merge(@RequestBody AddOrderVo user) {
//        User saveUser = user.getId() != null ? userRepository.findOne(user.getId()) : new User();
//        BeanUtils.copyNotNullProperties(user, saveUser);
//        return userRepository.merge(saveUser);
        return null;
    }

    @PostMapping("/delete")
    @Transactional
    public Set<Long> delete(String ids) {
//        Set<Long> result = new HashSet<>();
//        String[] idArr = ids.split(",");
//        if (ArrayUtils.isNotEmpty(idArr)) {
//            for (String id : idArr) {
//                if (StringUtils.isNotEmpty(id)) {
//                    userRepository.delete(Long.valueOf(id));
//                    result.add(Long.valueOf(id));
//                }
//            }
//        }
//        return result;
        return null;
    }
}
