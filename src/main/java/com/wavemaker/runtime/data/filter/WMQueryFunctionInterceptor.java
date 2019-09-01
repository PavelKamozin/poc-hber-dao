package com.wavemaker.runtime.data.filter;

import com.wavemaker.runtime.data.expression.HqlFunction;
import com.wavemaker.runtime.data.model.QueryInfo;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class WMQueryFunctionInterceptor implements QueryInterceptor {

    private static final int FUNCTION_NAME_GROUP = 1;
    private static final int VALUE_GROUP = 2;

    private static final String functionsPattern = "wm_(" + pipeSeparatedFunctions() + ")\\('([^']+)'\\)";
    private static final Pattern pattern = Pattern.compile(functionsPattern, Pattern.CASE_INSENSITIVE);

    @Override
    public void intercept(final QueryInfo queryInfo) {
        Matcher matcher = pattern.matcher(queryInfo.getQuery());
        StringBuffer newQuerySB = new StringBuffer();

        int parameterIndex = 1;
        while (matcher.find()) {
            HqlFunction function = HqlFunction.valueOf(matcher.group(FUNCTION_NAME_GROUP).toUpperCase());

            String parameterName = "param" + (parameterIndex++);

            matcher.appendReplacement(newQuerySB, ":" + parameterName);
            queryInfo.addParameter(parameterName, function.convertValue(matcher.group(VALUE_GROUP)));
        }
        matcher.appendTail(newQuerySB);
        queryInfo.setQuery(newQuerySB.toString());
    }

    private static String pipeSeparatedFunctions() {
        StringBuilder sb = new StringBuilder();
        HqlFunction[] values = HqlFunction.values();
        for (int i = 0, valuesLength = values.length; i < valuesLength; i++) {
            HqlFunction function = values[i];

            sb.append(function.name());

            if (i < valuesLength - 1) {
                sb.append("|");
            }
        }
        return sb.toString();
    }
}
