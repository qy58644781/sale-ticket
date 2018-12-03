package com.yadan.saleticket.controller.admin;

import com.yadan.saleticket.dao.hibernate.base.STPageRequest;
import com.yadan.saleticket.entity.AddProductVo;
import com.yadan.saleticket.enums.SexEnum;
import com.yadan.saleticket.enums.TicketTypeEnum;
import com.yadan.saleticket.model.Product.Product;
import com.yadan.saleticket.model.Theatre.Hall;
import com.yadan.saleticket.service.ProductService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/admin/product")
public class AdminProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("")
    public Page<Product> products(STPageRequest pageRequest, String filter) {
        return null;
    }

    @GetMapping("/{id}")
    public Product product(@PathVariable("id") Long id) {
        return null;
    }

    @PostMapping("/merge")
    public Product merge(@RequestBody AddProductVo vo) {
        return productService.createProduct(vo);
    }

    @GetMapping("/ticketTypeEnum")
    public List<Map> hallEnums() {
        List<Map> result = new ArrayList<>();
        for (TicketTypeEnum each : TicketTypeEnum.values()) {
            Map map = new HashMap();
            map.put("id", each);
            map.put("name", each.getVal());
            result.add(map);
        }
        return result;
    }

    @PostMapping("/delete")
    @Transactional
    public Set<Long> delete(String ids) {
        Set<Long> result = new HashSet<>();
        return result;
    }
}
