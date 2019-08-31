package softserve.hibernate.com.builder;

import com.wavemaker.runtime.data.filter.LegacyQueryFilterInterceptor;
import com.wavemaker.runtime.data.filter.QueryInterceptor;
import com.wavemaker.runtime.data.filter.WMQueryFunctionInterceptor;
import com.wavemaker.runtime.data.filter.WMQueryInfo;
import com.wavemaker.runtime.data.model.Aggregation;
import com.wavemaker.runtime.data.model.AggregationInfo;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static java.util.Objects.nonNull;

public class QueryBuilder {

    private static final List<QueryInterceptor> interceptors = asList(
            new LegacyQueryFilterInterceptor(),
            new WMQueryFunctionInterceptor());

    private static final String FROM = " from ";
    private static final String ORDER_BY = " order by ";
    private static final String GROUP_BY = " group by ";
    private static final String SELECT_COUNT1 = "select count(*) ";
    private static final String FROM_HQL = "FROM ";

    public static WMQueryInfo build(AggregationInfo aggregationInfo, String simpleName, String nameAlias) {

        StringBuilder builder = new StringBuilder();

        Map<String, Object> parameters = new HashMap<>();

        String projections = generateProjections(aggregationInfo);

        builder.append("select ");

        if (StringUtils.isNotBlank(projections)) {
            builder.append(projections);
        } else {
            builder.append(nameAlias);
        }

        builder.append(" ")
                .append("from ")
                .append(simpleName)
                .append(" ")
                .append(nameAlias)
                .append(" ");

        String filter = aggregationInfo.getFilter();

        if (StringUtils.isNotBlank(filter)) {
            final WMQueryInfo queryInfo = interceptFilter(filter);
            builder.append("where ")
                    .append(queryInfo.getQuery())
                    .append(" ");
            parameters = queryInfo.getParameters();
        }

        List<String> groupByFields = aggregationInfo.getGroupByFields();

        if (!groupByFields.isEmpty()) {
            builder.append("group by ")
                    .append(StringUtils.join(groupByFields, ","))
                    .append(" ");
        }

        return new WMQueryInfo(builder.toString(), parameters);
    }

    private static String generateProjections(AggregationInfo aggregationInfo) {
        List<String> projections = new ArrayList<>();

        List<String> groupByFields = aggregationInfo.getGroupByFields();

        if (!groupByFields.isEmpty()) {
            for (final String field : groupByFields) {
                projections.add(field + " as " + cleanAlias(field));
            }
        }

        List<Aggregation> aggregations = aggregationInfo.getAggregations();

        if (!aggregations.isEmpty()) {
            for (final Aggregation aggregation : aggregations) {
                projections.add(aggregation.asSelection());
            }
        }

        return StringUtils.join(projections, ",");
    }

    private static String cleanAlias(String alias) {
        return alias.replaceAll("\\.", "\\$");
    }

    private static WMQueryInfo interceptFilter(String filter) {
        WMQueryInfo queryInfo = new WMQueryInfo(filter);

        for (final QueryInterceptor interceptor : interceptors) {
            interceptor.intercept(queryInfo);
        }

        return queryInfo;
    }

    public static String getCountQuery(String query) {
        query = query.trim();

        String countQuery = null;
        int index = StringUtils.indexOfIgnoreCase(query, GROUP_BY);
        if (index == -1) {
            index = StringUtils.indexOfIgnoreCase(query, FROM_HQL);
            if (index >= 0) {
                if (index != 0) {
                    index = StringUtils.indexOfIgnoreCase(query, FROM);
                    if (index > 0) {
                        query = query.substring(index);
                    }
                }
                index = StringUtils.indexOfIgnoreCase(query, ORDER_BY);
                if (index >= 0) {
                    query = query.substring(0, index);
                }
                countQuery = SELECT_COUNT1 + query;
            }
        }
        return countQuery;
    }

    public static String configureParameters(String query, Map<String, Object> params) {
        StringBuilder builder = new StringBuilder(query);

        if (nonNull(params) && !params.isEmpty()) {
            params.forEach((key, value) -> {
                String from = ":" + key;
                String to = value instanceof Float ? value.toString() : "'" + value.toString() + "'";
                replace(builder, from, to);
            });
        }

        return builder.toString();
    }

    private static void replace(StringBuilder builder, String from, String to) {
        int index = builder.indexOf(from);
        builder.replace(index, index + from.length(), to);
    }

}
