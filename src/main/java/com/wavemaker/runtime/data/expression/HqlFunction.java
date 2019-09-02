package com.wavemaker.runtime.data.expression;


import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static softserve.hibernate.com.config.Constants.SQL.DATE_PATTERN;

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

    public abstract String convertValue(String fromValue);
}
