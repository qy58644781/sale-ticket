package com.yadan.saleticket.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yadan.saleticket.enums.CredentialTypeEnum;
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
@SQLDelete(sql = "update yd_st_buy_ticket_user_info set is_deleted=1,update_time=now() where id=?")
public class BuyTicketUserInfo extends BaseModel {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(name = "", value = ConstraintMode.NO_CONSTRAINT))
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Member member;

    /**
     * 真实姓名
     */
    private String realname;

    /**
     * 证件类型
     */
    @Column(length = 30)
    @Enumerated(EnumType.STRING)
    private CredentialTypeEnum credentialTypeEnum;

    /**
     * 证件号码
     */
    private String credentialNo;
}
