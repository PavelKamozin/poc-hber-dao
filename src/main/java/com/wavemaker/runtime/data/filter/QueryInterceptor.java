package com.wavemaker.runtime.data.filter;

public interface QueryInterceptor {

    void intercept(WMQueryInfo queryInfo);

}
