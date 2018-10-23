package com.yadan.saleticket.model.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yadan.saleticket.base.tools.LocalDateTimeDeserializer;
import com.yadan.saleticket.base.tools.LocalDateTimeSerializer;

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

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @Column(columnDefinition = "datetime")
    protected LocalDateTime createTime;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @Column(columnDefinition = "datetime")
    protected LocalDateTime updateTime;

    /**
     * 是否删除
     */
    @JsonIgnore
    protected Boolean isDeleted = false;

    /**
     * 版本
     */
    @JsonIgnore
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
