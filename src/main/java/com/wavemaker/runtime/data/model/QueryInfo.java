package com.wavemaker.runtime.data.model;

import java.util.HashMap;
import java.util.Map;

public class QueryInfo {

    private String query;
    private Map<String, Object> parameters;

    public QueryInfo(final String query) {
        this.query = query;
        this.parameters = new HashMap<>();
    }

    public QueryInfo(final String query, final Map<String, Object> parameters) {
        this.query = query;
        this.parameters = parameters;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(final String query) {
        this.query = query;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public void setParameters(final Map<String, Object> parameters) {
        this.parameters = parameters;
    }

    public void addParameter(String name, Object value) {
        this.getParameters().put(name, value);
    }

}
