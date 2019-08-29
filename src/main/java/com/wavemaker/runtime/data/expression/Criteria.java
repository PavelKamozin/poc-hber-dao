package com.wavemaker.runtime.data.expression;

import org.hibernate.criterion.Criterion;

public interface Criteria {

    Criterion criterion(final String name, final Object value);
}
