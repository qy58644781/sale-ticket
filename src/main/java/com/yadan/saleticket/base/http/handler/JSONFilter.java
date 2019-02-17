package com.yadan.saleticket.base.http.handler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;

/**
 * JSON属性过滤器
 * @author Cary
 * @date 2016/4/7
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JSONFilter {
    protected List<String> includes;
    protected List<String> excludes;

    public JSONFilter(String[] includes, String[] excludes) {
        this.includes = includes == null ? null : Arrays.asList(includes);
        this.excludes = excludes == null ? null : Arrays.asList(excludes);
    }
}
