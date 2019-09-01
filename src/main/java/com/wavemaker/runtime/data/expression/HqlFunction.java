package com.wavemaker.runtime.data.expression;


import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public enum HqlFunction {

    DT {
        @Override
        public String convertValue(String fromValue) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            return String.valueOf(LocalDateTime.parse(fromValue, formatter));
        }
    },

    TS {
        @Override
        public String convertValue(String fromValue) {
            return String.valueOf(new Timestamp(Long.valueOf(fromValue)));
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
