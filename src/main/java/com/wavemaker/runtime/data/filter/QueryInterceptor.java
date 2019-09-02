package com.wavemaker.runtime.data.filter;

import com.wavemaker.runtime.data.model.QueryInfo;

public interface QueryInterceptor {

    void intercept(QueryInfo queryInfo);

}
