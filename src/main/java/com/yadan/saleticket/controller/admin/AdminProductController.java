package com.yadan.saleticket.controller.admin;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/product")
public class AdminProductController {

    @RequestMapping("/open/test1")
    public String test() {
        return "i am open";
    }

    @RequestMapping("/test2")
    public String test2() {
        return "i am not open";
    }
}
