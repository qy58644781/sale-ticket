package com.yadan.saleticket.entity.wx;

import com.yadan.saleticket.base.tools.BeanUtils;
import com.yadan.saleticket.entity.app.AppMemberVO;
import lombok.Data;

@Data
public class WxMemberVO extends AppMemberVO {

    public WxMemberVO(AppMemberVO appMemberVO, Boolean needRegister) {
        BeanUtils.copyNotNullProperties(appMemberVO, this);
        this.needRegister = needRegister;
    }

    private Boolean needRegister = false;

}
