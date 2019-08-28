package com.wavemaker.runtime.data.expression;

import org.hibernate.type.BigDecimalType;
import org.hibernate.type.BigIntegerType;
import org.hibernate.type.BlobType;
import org.hibernate.type.BooleanType;
import org.hibernate.type.ByteType;
import org.hibernate.type.CharacterType;
import org.hibernate.type.ClobType;
import org.hibernate.type.CurrencyType;
import org.hibernate.type.DoubleType;
import org.hibernate.type.FloatType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LocaleType;
import org.hibernate.type.LongType;
import org.hibernate.type.ShortType;
import org.hibernate.type.StringType;
import org.hibernate.type.TextType;
import org.hibernate.type.TimeZoneType;
import org.hibernate.type.TrueFalseType;
import org.hibernate.type.YesNoType;

import java.sql.Date;
import java.sql.Timestamp;

public enum AttributeType implements TypeConverter {

    BIG_DECIMAL {
        @Override
        public Object toJavaType(final Object value) {
            return BigDecimalType.INSTANCE.fromString(value.toString());
        }
    },
    BIG_INTEGER {
        @Override
        public Object toJavaType(final Object value) {
            return BigIntegerType.INSTANCE.fromString(value.toString());
        }
    },
    BLOB {
        @Override
        public Object toJavaType(final Object value) {
            return BlobType.INSTANCE.fromString(value.toString());
        }
    },
    BOOLEAN {
        @Override
        public Object toJavaType(final Object value) {
            return BooleanType.INSTANCE.fromString(value.toString());
        }
    },
    BYTE {
        @Override
        public Object toJavaType(final Object value) {
            return ByteType.INSTANCE.fromString(value.toString());
        }
    },
    CALENDAR {
        @Override
        public Object toJavaType(final Object value) {
            return new Date(((Number) value).longValue());
        }
    },
    CALENDAR_DATE {
        @Override
        public Object toJavaType(final Object value) {
            return new Date(((Number) value).longValue());
        }
    },
    CHARACTER {
        @Override
        public Object toJavaType(final Object value) {
            return CharacterType.INSTANCE.fromString(value.toString());
        }
    },
    CLOB {
        @Override
        public Object toJavaType(final Object value) {
            return ClobType.INSTANCE.fromString(value.toString());
        }
    },
    CURRENCY {
        @Override
        public Object toJavaType(final Object value) {
            return CurrencyType.INSTANCE.fromString(value.toString());
        }
    },
    DATE {
        @Override
        public Object toJavaType(final Object value) {
            if (value instanceof Number) {
                return new Date(((Number) value).longValue());
            } else {
                // return WMDateDeSerializer.getDate(value.toString());
                return new Date(Long.valueOf(value.toString()));
            }
        }
    },
    DOUBLE {
        @Override
        public Object toJavaType(final Object value) {
            return DoubleType.INSTANCE.fromString(value.toString());
        }
    },
    FLOAT {
        @Override
        public Object toJavaType(final Object value) {
            return FloatType.INSTANCE.fromString(value.toString());
        }
    },
    INTEGER {
        @Override
        public Object toJavaType(final Object value) {
            return IntegerType.INSTANCE.fromString(value.toString());
        }
    },
    LONG {
        @Override
        public Object toJavaType(final Object value) {
            return LongType.INSTANCE.fromString(value.toString());
        }
    },
    LOCALE {
        @Override
        public Object toJavaType(final Object value) {
            return LocaleType.INSTANCE.fromString(value.toString());
        }
    },
    STRING {
        @Override
        public Object toJavaType(final Object value) {
            return StringType.INSTANCE.fromString(value.toString());
        }
    },
    SHORT {
        @Override
        public Object toJavaType(final Object value) {
            return ShortType.INSTANCE.fromString(value.toString());
        }
    },
    TEXT {
        @Override
        public Object toJavaType(final Object value) {
            return TextType.INSTANCE.fromString(value.toString());
        }
    },
    TIME {
        @Override
        public Object toJavaType(final Object value) {
            if (value instanceof Number) {
                return new java.sql.Time(((Number) value).longValue());
            } else {
                return new Date(Long.valueOf(value.toString()));
                //return WMDateDeSerializer.getDate((String) value);
            }
        }
    },
    DATETIME {
        @Override
        public Object toJavaType(final Object value) {
            return new Date(Long.valueOf(value.toString()));
            //return WMLocalDateTimeDeSerializer.getLocalDateTime((String) value);
        }
    },
    TIMESTAMP {
        @Override
        public Object toJavaType(final Object value) {
            return new Timestamp(((Number) value).longValue());
        }
    },
    TIMEZONE {
        @Override
        public Object toJavaType(final Object value) {
            return TimeZoneType.INSTANCE.fromString(value.toString());
        }
    },
    TRUE_FALSE {
        @Override
        public Object toJavaType(final Object value) {
            return TrueFalseType.INSTANCE.fromString(value.toString());
        }
    },
    YES_NO {
        @Override
        public Object toJavaType(final Object value) {
            return YesNoType.INSTANCE.fromString(value.toString());
        }
    };

    public String getHibernateType() {
        return this.name().toLowerCase();
    }
}
