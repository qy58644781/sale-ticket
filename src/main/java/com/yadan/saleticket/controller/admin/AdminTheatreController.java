package com.yadan.saleticket.controller.admin;

import com.yadan.saleticket.dao.hibernate.HallRepository;
import com.yadan.saleticket.dao.hibernate.SeatRepository;
import com.yadan.saleticket.dao.hibernate.TheatreRepository;
import com.yadan.saleticket.model.Theatre.Hall;
import com.yadan.saleticket.model.Theatre.Theatre;
import com.yadan.saleticket.service.HallSeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/admin/theatre")
public class AdminTheatreController {

    @Autowired
    private HallSeatService hallSeatService;
    @Autowired
    private TheatreRepository theatreRepository;
    @Autowired
    private HallRepository hallRepository;
    @Autowired
    private SeatRepository seatRepository;

    @PostMapping("/open/hall/create")
    @Transactional
    public void uploadSeats(MultipartFile file) throws IOException {
        Theatre theatre = new Theatre();
        theatre.setName("my theatre");
        theatreRepository.save(theatre);

        Hall hall = new Hall();
        hall.setName("my hall");
        hall.setTheatre(theatre);

        hallSeatService.createSeatByExcel(file.getInputStream(), hall);
    }
}
