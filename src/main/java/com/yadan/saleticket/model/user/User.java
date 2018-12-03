package com.yadan.saleticket.model.user;

import com.yadan.saleticket.enums.SexEnum;
import com.yadan.saleticket.model.base.BaseModel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Where(clause = "is_deleted=0")
@SQLDelete(sql = "update yd_st_user set is_deleted=1,update_time=now() where id=?")
public class User extends BaseModel {

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 用户手机/账户
     */
    private String mobile;

    /**
     * 密码
     */
    private String password;

    /**
     * 用户性别
     */
    @Enumerated(EnumType.STRING)
    private SexEnum sexEnum;

}
