package com.yadan.saleticket.model.theatre;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.yadan.saleticket.model.base.BaseModel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

/**
 * 剧场影院
 */
@Entity
@Getter
@Setter
@Where(clause = "is_deleted=0")
@SQLDelete(sql = "update yd_st_Theatre set is_deleted=1,update_time=now() where id=?")
public class Theatre extends BaseModel {

    /**
     * 剧场名称
     */
    private String name;

    @OneToMany(mappedBy = "theatre", cascade = CascadeType.REMOVE)
    @Where(clause = "is_deleted=0")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JsonManagedReference
    private List<Hall> halls;
}
