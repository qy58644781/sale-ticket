package com.yadan.saleticket.service;

import com.yadan.saleticket.entity.HallSeatVo;
import org.springframework.stereotype.Service;

@Service
public class HallSeatService {

    public HallSeatVo genHallSeatVo(Long hallId) {
        HallSeatVo vo = new HallSeatVo();
        return vo;
    }

    /**
     * 根据excel生成剧院座位图
     */
    public void createSeatByExcel() {

    }
}
