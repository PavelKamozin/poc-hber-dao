package com.wavemaker.runtime.data.expression;

import com.wavemaker.runtime.data.util.QueryParserConstants;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public enum Type implements Criteria {

    STARTING_WITH("startswith") {
        @Override
        public Criterion criterion(final String name, final Object value) {
            return Restrictions.ilike(name, String.valueOf(value), MatchMode.START);
        }
    }, ENDING_WITH("endswith") {
        @Override
        public Criterion criterion(final String name, final Object value) {
            return Restrictions.ilike(name, String.valueOf(value), MatchMode.END);
        }
    }, CONTAINING("containing") {
        @Override
        public Criterion criterion(final String name, final Object value) {
            return Restrictions.ilike(name, String.valueOf(value), MatchMode.ANYWHERE);
        }
    }, EQUALS("=") {
        @Override
        public Criterion criterion(final String name, final Object value) {
            if (value == null) {
                throw new IllegalArgumentException("Equals expression should not have null value, either collection or array or primitive values supported.");
            }

            Criterion criterion;
            if (value instanceof Collection) {
                final Collection values = (Collection) value;
                if (values.size() == 0) {
                    throw new IllegalArgumentException("Equals expression should have a collection/array of values with at-least one entry.");
                }
                criterion = Restrictions.in(name, values);
            } else if (value.getClass().isArray()) {
                final Object[] values = (Object[]) value;
                if (values.length == 0) {
                    throw new IllegalArgumentException("Equals expression should have a collection/array of values with at-least one entry.");
                }
                criterion = Restrictions.in(name, values);
            } else {
                criterion = Restrictions.eq(name, value);
            }
            return criterion;
        }
    }, NOT_EQUALS("!=") {
        @Override
        public Criterion criterion(final String name, final Object value) {
            return Restrictions.ne(name, value);
        }
    }, BETWEEN("between") {
        @Override
        public Criterion criterion(final String name, final Object value) {
            Criterion criterion;
            if (value instanceof Collection) {
                Collection collection = (Collection) value;
                if (collection.size() != 2)
                    throw new IllegalArgumentException("Between expression should have a collection/array of values with just two entries.");

                Iterator iterator = collection.iterator();
                criterion = Restrictions.between(name, iterator.next(), iterator.next());
            } else if (value.getClass().isArray()) {
                Object[] array = (Object[]) value;
                if (array.length != 2)
                    throw new IllegalArgumentException("Between expression should have a array/array of values with just two entries.");

                criterion = Restrictions.between(name, array[0], array[1]);
            } else {
                throw new IllegalArgumentException("Between expression should have a collection/array of values with just two entries.");
            }
            return criterion;
        }
    }, LESS_THAN("<") {
        @Override
        public Criterion criterion(final String name, final Object value) {
            return Restrictions.lt(name, value);
        }
    }, LESS_THAN_OR_EQUALS("<=") {
        @Override
        public Criterion criterion(final String name, final Object value) {
            return Restrictions.le(name, value);
        }
    }, GREATER_THAN(">") {
        @Override
        public Criterion criterion(final String name, final Object value) {
            return Restrictions.gt(name, value);
        }
    }, GREATER_THAN_OR_EQUALS(">=") {
        @Override
        public Criterion criterion(final String name, final Object value) {
            return Restrictions.ge(name, value);
        }
    }, NULL("null") {
        @Override
        public Criterion criterion(final String name, final Object value) {
            return Restrictions.isNull(name);
        }
    }, EMPTY("empty") {
        @Override
        public Criterion criterion(final String name, final Object value) {
            return Restrictions.eq(name, "");
        }
    }, LIKE("like") {
        @Override
        public Criterion criterion(String name, Object value) {
            return Restrictions.like(name, value);
        }
    }, IN("in") {
        @Override
        public Criterion criterion(String name, Object value) {
            if (value instanceof Collection) {
                return Restrictions.in(name, (Collection) value);
            }
            throw new RuntimeException("Expected Collection type but found value of type " + value.getClass());
        }
    }, NULL_OR_EMPTY("nullorempty") {
        @Override
        public Criterion criterion(final String name, final Object value) {
            Criterion emptyValueCriterion = Restrictions.eq(name, "");
            Criterion nullValueCriterion = Restrictions.isNull(name);
            return Restrictions.or(emptyValueCriterion, nullValueCriterion);
        }
    }, IS("is") {
        @Override
        public Criterion criterion(String name, Object value) {
            String castedValue = (String) value;
            if (QueryParserConstants.NULL.equalsIgnoreCase(castedValue)) {
                return NULL.criterion(name, value);
            } else if (QueryParserConstants.NOTNULL.equalsIgnoreCase(castedValue)) {
                return Restrictions.not(NULL.criterion(name, value));
            } else if (QueryParserConstants.NULL_OR_EMPTY.equalsIgnoreCase(castedValue)) {
                return NULL_OR_EMPTY.criterion(name, value);
            } else if (QueryParserConstants.EMPTY.equalsIgnoreCase(castedValue)) {
                return EMPTY.criterion(name, value);
            }
            throw new RuntimeException("Value for IS operator can be either of null, notnull, nullorempty, empty" + value.getClass());
        }
    };


    static Map<String, Type> nameVsType = new HashMap<>();

    static {
        for (Type type : Type.values()) {
            nameVsType.put(type.getName(), type);
        }
    }

    private String name;

    Type(String name) {
        this.name = name;
    }

    public static Type valueFor(String typeName) {
        return nameVsType.get(typeName.toLowerCase());

    }

    public String getName() {
        return name;
    }
}
