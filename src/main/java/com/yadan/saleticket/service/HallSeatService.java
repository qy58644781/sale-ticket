package com.yadan.saleticket.service;

import com.yadan.saleticket.base.exception.ExceptionCode;
import com.yadan.saleticket.base.exception.ServiceException;
import com.yadan.saleticket.base.tools.ExcelUtils;
import com.yadan.saleticket.dao.hibernate.HallRepository;
import com.yadan.saleticket.dao.hibernate.SeatRepository;
import com.yadan.saleticket.entity.HallSeatsVo;
import com.yadan.saleticket.model.Theatre.Hall;
import com.yadan.saleticket.model.Theatre.Seat;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class HallSeatService {

    @Autowired
    private HallRepository hallRepository;

    @Autowired
    private SeatRepository seatRepository;

    /**
     * 根据剧院座位生成list列表
     * @param hallId
     * @return
     */
    public HallSeatsVo genHallSeatVo(Long hallId) {
        HallSeatsVo vo = new HallSeatsVo();
        return vo;
    }

    /**
     * 根据excel创建剧院座位
     */
    @Transactional
    public void createSeatByExcel(InputStream is, Hall hall) {

        log.debug("开始导入 hallId: " + hall.getId() + " 的excel");

        XSSFWorkbook excel = null;
        // 最大列
        short maxColumn = 0;
        // 最大行
        int maxRow = 0;

        List<Seat> saveSeats = new ArrayList<>();

        try {
            excel = new XSSFWorkbook(is);
            XSSFSheet sheet = excel.getSheetAt(0);

            int lastLineIndex = sheet.getLastRowNum();
            if (lastLineIndex < 0) {
                throw new ServiceException(ExceptionCode.INVALID_EXCEL_DATA, "模板数据有误，第一个sheet的内容为空");
            }
            maxRow = lastLineIndex + 1;

            for (int i = 0; i <= lastLineIndex; i++) {
                XSSFRow row = sheet.getRow(i);
                short lastCellNum = row.getLastCellNum();
                if (lastCellNum > maxColumn) maxColumn = lastCellNum;

                if (lastCellNum <= 0) {
                    continue;
                }
                for (int j = 0; j < lastCellNum; j++) {
                    XSSFCell cell = row.getCell(j);

                    Object value = ExcelUtils.getValue(cell);
                    if (value != null) {
                        Seat seat = new Seat();
                        seat.setValid(true);
                        seat.setAreaName(value.toString());
                        seat.setSeatColumn(i);
                        seat.setSeatRow(j);
                        seat.setHall(hall);
                        saveSeats.add(seat);

                        log.debug("x: " + i + ", y: " + j + ", val: " + value + "|");
                    }
                }
                log.debug("");

            }

            // 更新座位
            seatRepository.save(saveSeats);

            hall.setMaxColumn(maxRow);
            hall.setMaxRow(maxRow);
            hallRepository.merge(hall);

            log.info("导入 hallId: " + hall.getId() + " 的excel座位成功");

        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            if (excel != null) {
                try {
                    excel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    log.error(e.getMessage());
                }
            }

            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    log.error(e.getMessage());
                }
            }
        }
    }
}
