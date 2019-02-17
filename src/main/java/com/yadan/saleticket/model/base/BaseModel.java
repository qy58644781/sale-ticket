package com.yadan.saleticket.model.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import flexjson.JSON;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by wujun on 2017/11/10.
 * 所有model的继承类
 */
@Getter
@Setter
@MappedSuperclass
public class BaseModel {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(columnDefinition = "datetime")
    protected LocalDateTime createTime;

    @Column(columnDefinition = "datetime")
    protected LocalDateTime updateTime;

    /**
     * 是否删除
     */
    @JsonIgnore
    @JSON(include = false)
    protected Boolean isDeleted = false;

    /**
     * 版本
     */
    @JsonIgnore
    @JSON(include = false)
    protected long version;

    @PrePersist
    public void prePersist() {
        if (createTime == null) {
            createTime = LocalDateTime.now();
        }
        if (updateTime == null) {
            updateTime = LocalDateTime.now();
        }
    }

    @PreUpdate
    public void preUpdate() {
        updateTime = LocalDateTime.now();
    }

}
