package com.wavemaker.runtime.data.expression;


import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public enum HqlFunction {

    DT {
        @Override
        public String convertValue(String fromValue) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
            return "'" + LocalDateTime.parse(fromValue, formatter) + "'";
        }
    },

    TS {
        @Override
        public String convertValue(String fromValue) {
            return "'" + new Timestamp(Long.valueOf(fromValue)) + "'";
        }
    },

    FLOAT {
        @Override
        public String convertValue(String fromValue) {
            return String.valueOf(Float.valueOf(fromValue));
        }
    };

    public static final String DATE_PATTERN = "yyyy-MM-dd HH:mm";
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public abstract String convertValue(String fromValue);
}
