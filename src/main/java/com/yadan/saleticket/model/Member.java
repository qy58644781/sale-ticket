package com.yadan.saleticket.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yadan.saleticket.enums.CredentialTypeEnum;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Where(clause = "is_deleted=0")
@SQLDelete(sql = "update yd_st_user set is_deleted=1,update_time=now() where id=?")
public class Member extends User {

    private String openId;

    private String unionId;

    private String avatarUrl;

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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "default_address_id", foreignKey = @ForeignKey(name = "", value = ConstraintMode.NO_CONSTRAINT))
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private MailAddress defaultAddress;


}
