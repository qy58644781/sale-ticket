package com.yadan.saleticket.dao.hibernate.base;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@Getter
@Setter
public class STPageRequest {
    private String sortField;
    private Sort.Direction sortOrder;
    private Integer page;
    private Integer count;
    private String filter;

    public PageRequest genPageRequest() {
        if (StringUtils.isEmpty(this.getSortField()) && this.getSortOrder() != null) {
            return new PageRequest(this.getPage() - 1, this.getCount(), this.getSortOrder());
        } else if (this.getSortOrder() == null) {
            return new PageRequest(this.getPage() - 1, this.getCount());
        } else {
            return new PageRequest(this.getPage() - 1, this.getCount(),
                    this.getSortOrder(), this.getSortField());
        }
    }

}
