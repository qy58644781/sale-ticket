package com.yadan.saleticket.base.http;

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

    public STResponse(T result) {
        this.result = result;
    }

}
