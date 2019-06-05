package com.yadan.saleticket.controller.admin;

import com.yadan.saleticket.base.tools.BeanUtils;
import com.yadan.saleticket.base.tools.Json;
import com.yadan.saleticket.dao.hibernate.HallRepository;
import com.yadan.saleticket.dao.hibernate.SeatRepository;
import com.yadan.saleticket.dao.hibernate.TheatreRepository;
import com.yadan.saleticket.dao.hibernate.base.STPageRequest;
import com.yadan.saleticket.entity.AddUserVo;
import com.yadan.saleticket.entity.PageVo;
import com.yadan.saleticket.model.theatre.Theatre;
import com.yadan.saleticket.model.user.User;
import com.yadan.saleticket.service.HallSeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/admin/theatre")
public class AdminTheatreController {

    @Autowired
    private TheatreRepository theatreRepository;

    @GetMapping("")
    public PageVo<Theatre> theatres(STPageRequest pageRequest) {
//        PageVo<Theatre> theatres = theatreRepository.findAllByFilterAndPageRequest(pageRequest);
//        return theatres;
        return null;
    }

    @GetMapping("/{id}")
    public Theatre theatre(@PathVariable("id") Long id) {
        return theatreRepository.getOne(id);
    }

    @PostMapping("/merge")
    public Theatre merge(@RequestBody Theatre theatre) {
        Theatre saveTheatre = theatre.getId() != null ? theatreRepository.getOne(theatre.getId()) : new Theatre();
        BeanUtils.copyNotNullProperties(theatre, saveTheatre);
        return theatreRepository.save(saveTheatre);
    }

}
