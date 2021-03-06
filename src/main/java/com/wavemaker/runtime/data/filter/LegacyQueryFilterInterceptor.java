package com.wavemaker.runtime.data.filter;

import com.wavemaker.runtime.data.expression.Type;
import com.wavemaker.runtime.data.model.QueryInfo;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class LegacyQueryFilterInterceptor implements QueryInterceptor {

    private static final String LEGACY_QUERY_EXPRESSION = "([\\w]+)[\\s]+(startswith|endswith|containing)[\\s][\"']([^']+)+([\"'])";
    private static Pattern legacyQueryPattern = Pattern.compile(LEGACY_QUERY_EXPRESSION);

    private static final String WILDCARD_ENTRY = "%";

    private static final byte FIELD_NAME = 1;
    private static final byte EXPRESSION = 2;
    private static final byte VALUE = 3;

    @Override
    public void intercept(final QueryInfo queryInfo) {
        queryInfo.setQuery(replaceExpressionWithHQL(queryInfo.getQuery()));
    }

    private String replaceExpressionWithHQL(String query) {
        Matcher matcher = legacyQueryPattern.matcher(query);
        StringBuffer hqlQuery = new StringBuffer();
        while (matcher.find()) {
            String value = "";
            switch (Type.valueFor(matcher.group(EXPRESSION))) {
                case STARTING_WITH:
                    value = matcher.group(VALUE) + WILDCARD_ENTRY;
                    break;
                case ENDING_WITH:
                    value = WILDCARD_ENTRY + matcher.group(VALUE);
                    break;
                case CONTAINING:
                    value = WILDCARD_ENTRY + matcher.group(VALUE) + WILDCARD_ENTRY;
                    break;
            }
            matcher.appendReplacement(hqlQuery, matcher.group(FIELD_NAME) + " like " + "'" + value + "'");
        }
        matcher.appendTail(hqlQuery);
        return hqlQuery.toString();
    }
}
