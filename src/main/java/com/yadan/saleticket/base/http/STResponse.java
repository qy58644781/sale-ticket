package com.yadan.saleticket.base.http;

import com.yadan.saleticket.base.http.handler.JSONFilter;
import flexjson.JSON;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class STResponse<T> {
    private ResponseError error;
    private String requestId;
    private T result;
    @JSON(include = false)
    private JSONFilter jsonFilter;

    public STResponse(T result) {
        this.result = result;
    }

    public STResponse(T result, JSONFilter jsonFilter) {
        this.result = result;
        this.jsonFilter = jsonFilter;
    }
}
