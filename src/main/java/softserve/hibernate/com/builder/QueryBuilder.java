package softserve.hibernate.com.builder;

import com.wavemaker.runtime.data.filter.LegacyQueryFilterInterceptor;
import com.wavemaker.runtime.data.filter.QueryFunctionInterceptor;
import com.wavemaker.runtime.data.filter.QueryInterceptor;
import com.wavemaker.runtime.data.model.Aggregation;
import com.wavemaker.runtime.data.model.AggregationInfo;
import com.wavemaker.runtime.data.model.QueryInfo;
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
            new QueryFunctionInterceptor()
    );

    private static final String FROM = " from ";
    private static final String ORDER_BY = " order by ";
    private static final String GROUP_BY = " group by ";
    private static final String SELECT_COUNT = "select count(*) ";

    public static QueryInfo build(AggregationInfo aggregationInfo, String simpleName, String nameAlias) {

        StringBuilder builder = new StringBuilder();

        String projections = generateProjections(aggregationInfo);

        builder.append("select ")
                .append(StringUtils.isNotBlank(projections) ? projections : nameAlias)
                .append(" from ")
                .append(simpleName)
                .append(" ")
                .append(nameAlias);

        String filter = aggregationInfo.getFilter();

        Map<String, Object> parameters = new HashMap<>();

        if (StringUtils.isNotBlank(filter)) {
            QueryInfo queryInfo = interceptFilter(filter);
            builder.append(" where ")
                    .append(queryInfo.getQuery());
            parameters = queryInfo.getParameters();
        }

        List<String> groupByFields = aggregationInfo.getGroupByFields();

        if (nonNull(groupByFields) && !groupByFields.isEmpty()) {
            builder.append(" group by ")
                    .append(StringUtils.join(groupByFields, ","));
        }

        return new QueryInfo(builder.toString(), parameters);
    }

    private static String generateProjections(AggregationInfo aggregationInfo) {
        List<String> projections = new ArrayList<>();

        List<String> groupByFields = aggregationInfo.getGroupByFields();

        if (nonNull(groupByFields) && !groupByFields.isEmpty()) {
            for (String field : groupByFields) {
                projections.add(field + " as " + cleanAlias(field));
            }
        }

        List<Aggregation> aggregations = aggregationInfo.getAggregations();

        if (nonNull(aggregations) && !aggregations.isEmpty()) {
            for (Aggregation aggregation : aggregations) {
                projections.add(aggregation.asSelection());
            }
        }

        return StringUtils.join(projections, ",");
    }

    private static String cleanAlias(String alias) {
        return alias.replaceAll("\\.", "\\$");
    }

    private static QueryInfo interceptFilter(String filter) {
        QueryInfo queryInfo = new QueryInfo(filter);

        for (QueryInterceptor interceptor : interceptors) {
            interceptor.intercept(queryInfo);
        }

        return queryInfo;
    }

    public static String getCountQuery(String query) {
        String countQuery = null;
        int index = StringUtils.indexOfIgnoreCase(query, GROUP_BY);
        if (index == -1) {
            index = StringUtils.indexOfIgnoreCase(query, FROM);
            if (index > 0) {
                query = query.substring(index);
                index = StringUtils.indexOfIgnoreCase(query, ORDER_BY);
                if (index >= 0) {
                    query = query.substring(0, index);
                }
                countQuery = SELECT_COUNT + query;
            }
        }
        return countQuery;
    }

    public static String configureParameters(String query, Map<String, Object> params) {
        StringBuilder builder = new StringBuilder(query);

        if (nonNull(params) && !params.isEmpty()) {
            params.forEach((key, value) -> replace(builder, ":" + key, value.toString()));
        }

        return builder.toString();
    }

    private static void replace(StringBuilder builder, String from, String to) {
        int index = builder.indexOf(from);
        builder.replace(index, index + from.length(), to);
    }

}
