package com.yadan.saleticket.model;

import com.yadan.saleticket.model.base.BaseModel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;

@Entity
@Getter
@Setter
@Where(clause = "is_deleted=0")
public class WxMaJscode2Session extends BaseModel {
    private String sessionKey;

    private String openId;

    private String unionId;
}
