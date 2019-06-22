package com.yadan.saleticket.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yadan.saleticket.model.base.BaseModel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Where(clause = "is_deleted=0")
@SQLDelete(sql = "update yd_st_mail_address set is_deleted=1,update_time=now() where id=?")
public class MailAddress extends BaseModel {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(name = "", value = ConstraintMode.NO_CONSTRAINT))
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Member member;

    private String province;

    private String city;

    private String area;

    private String postCode;

    @Column(columnDefinition = "text")
    private String addressDetail;

    private String linkman;

    private String phone;

    private String tags;
}
