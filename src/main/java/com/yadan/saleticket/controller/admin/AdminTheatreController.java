package com.yadan.saleticket.controller.admin;

import com.yadan.saleticket.base.exception.ExceptionCode;
import com.yadan.saleticket.base.exception.ServiceException;
import com.yadan.saleticket.dao.hibernate.HallRepository;
import com.yadan.saleticket.dao.hibernate.SeatRepository;
import com.yadan.saleticket.dao.hibernate.TheatreRepository;
import com.yadan.saleticket.model.Theatre.Hall;
import com.yadan.saleticket.model.Theatre.Theatre;
import com.yadan.saleticket.service.HallSeatService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @GetMapping("/hallEnums")
    public List<Map> hallEnums() {
        List<Map> result = new ArrayList<>();
        List<Hall> all = hallRepository.findAll();
        if (CollectionUtils.isNotEmpty(all)) {
            all.forEach(each -> {
                Map map = new HashMap();
                map.put("id", each.getId());
                map.put("name", each.getTheatre().getName() + " " + each.getName());
                result.add(map);
            });
        }
        return result;
    }

    @GetMapping("/exportHallExcel")
    public void exportHallExcel(Long hallId, Boolean onlineSale,  HttpServletResponse response) throws IOException {
        Hall hall = hallRepository.findOne(hallId);
        if (onlineSale == null) {
            throw new ServiceException(ExceptionCode.INVALID_EXCEL_DATA, "exportHallExcel的导出类型为空");
        }
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        if (onlineSale) {
            hallSeatService.createExcelBySeat(hall).write(os);
        } else {
            hallSeatService.createExcelForOffline().write(os);
        }
        byte[] content = os.toByteArray();
        response.setContentLength(content.length);
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + new String((hall.getName() + ".xlsx").getBytes(), "iso-8859-1"));
        ServletOutputStream out = response.getOutputStream();
        StreamUtils.copy(content, response.getOutputStream());
        out.close();
        response.flushBuffer();
    }
}
