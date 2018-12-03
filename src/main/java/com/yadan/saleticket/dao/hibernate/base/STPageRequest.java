package com.yadan.saleticket.dao.hibernate.base;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@Getter
@Setter
public class STPageRequest {
    private String sortField;
    private Sort.Direction sortOrder;
    private Integer page;
    private Integer count;
    private String filer;

    public PageRequest genPageRequest() {
        return new PageRequest(this.getPage() - 1, this.getCount(),
                this.getSortOrder(), this.getSortField());
    }

}
