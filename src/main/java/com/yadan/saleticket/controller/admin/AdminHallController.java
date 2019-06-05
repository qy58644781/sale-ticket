package com.yadan.saleticket.controller.admin;

import com.yadan.saleticket.base.exception.ExceptionCode;
import com.yadan.saleticket.base.exception.ServiceException;
import com.yadan.saleticket.base.http.STResponse;
import com.yadan.saleticket.base.http.handler.JSONFilter;
import com.yadan.saleticket.dao.hibernate.HallRepository;
import com.yadan.saleticket.dao.hibernate.TheatreRepository;
import com.yadan.saleticket.dao.hibernate.base.STPageRequest;
import com.yadan.saleticket.entity.AddHallVo;
import com.yadan.saleticket.model.theatre.Hall;
import com.yadan.saleticket.service.HallSeatService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/hall")
public class AdminHallController {

    private static final JSONFilter jsonFilter = new JSONFilter(new String[]{
            "*.id",
            "*.name",
    }, new String[]{
    });

    @Autowired
    private HallRepository hallRepository;
    @Autowired
    private HallSeatService hallSeatService;
    @Autowired
    private TheatreRepository theatreRepository;

    @GetMapping("")
    public STResponse<Hall> halls(STPageRequest pageRequest) {
//        PageVo<Hall> halls = hallRepository.findAllByFilterAndPageRequest(pageRequest);
//        return new STResponse(halls, jsonFilter);
        return null;
    }

    @PostMapping("/merge")
    @Transactional
    public Hall merge(@RequestBody AddHallVo addHallVo) {
        return hallSeatService.mergeHall(addHallVo);
    }

    @GetMapping("/{id}")
    public STResponse<Hall> hall(@PathVariable("id") Long id) {
        return new STResponse<>(hallRepository.getOne(id), jsonFilter);
    }


    @GetMapping("/hallFullName")
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

    /**
     * 导出录入票价时候的模板
     *
     * @param hallId
     * @param onlineSale
     * @param response
     * @throws IOException
     */
    @GetMapping("/exportHallExcel")
    public void exportHallExcel(Long hallId, Boolean onlineSale, HttpServletResponse response) throws IOException {
        Hall hall = hallRepository.getOne(hallId);
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
