package com.wavemaker.runtime.data.filter;


import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public enum WMHqlFunction {
    DT {
        @Override
        public Object convertValue(String fromValue) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            return LocalDateTime.parse(fromValue, formatter);
        }
    },
    TS {
        @Override
        public Object convertValue(String fromValue) {
            return new Timestamp(Long.valueOf(fromValue));
        }
    },
    FLOAT {
        @Override
        public Object convertValue(String fromValue) {
            return Float.valueOf(fromValue);
        }
    };

    public abstract Object convertValue(String fromValue);
}
