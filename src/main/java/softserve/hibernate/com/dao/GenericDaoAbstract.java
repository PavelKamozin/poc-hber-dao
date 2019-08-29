package softserve.hibernate.com.dao;

import com.wavemaker.runtime.data.expression.QueryFilter;
import com.wavemaker.runtime.data.filter.LegacyQueryFilterInterceptor;
import com.wavemaker.runtime.data.filter.QueryInterceptor;
import com.wavemaker.runtime.data.filter.WMQueryFunctionInterceptor;
import com.wavemaker.runtime.data.filter.WMQueryInfo;
import com.wavemaker.runtime.data.model.Aggregation;
import com.wavemaker.runtime.data.model.AggregationInfo;
import com.wavemaker.runtime.data.util.CriteriaUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.query.internal.QueryImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.orm.hibernate5.HibernateCallback;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.*;

import static java.util.Objects.nonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.Objects.nonNull;

public abstract class GenericDaoAbstract<Entity extends Serializable, Identifier extends Serializable> implements GenericDao<Entity, Identifier> {

    private static final List<QueryInterceptor> interceptors = Arrays.asList(
            new LegacyQueryFilterInterceptor(),
            new WMQueryFunctionInterceptor());

    private static final String SELECT = " select ";
    private static final String FROM = " from ";
    private static final String WHERE = " where ";
    private static final String IN = " in ";
    private static final String ID = "id ";
    private static final String ORDER_BY = " order by ";
    private static final String WHITE_SPACE = " ";
    private static final String COUNT_QUERY_TEMPLATE = "select count(*) from ({0}) wmTempTable";
    private static final String GROUP_BY = " group by ";
    private static final String SELECT_COUNT1 = "select count(*) ";
    private static final String FROM_HQL = "FROM ";

    private EntityManager entityManager;
    private Class<Entity> entityClass;
    private JpaRepository<Entity, Identifier> repository;

    @SuppressWarnings("unchecked")
    public GenericDaoAbstract(JpaRepository<Entity, Identifier> repository, EntityManager entityManager) {
        this.entityManager = entityManager;
        this.repository = repository;

        ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
        this.entityClass = (Class<Entity>) genericSuperclass.getActualTypeArguments()[0];
    }

    @Override
    public Entity create(Entity entity) {
        return repository.save(entity);
    }

    @Override
    public void update(Entity entity) {
        repository.save(entity);
    }

    @Override
    public void delete(Entity entity) {
        repository.delete(entity);
    }

    @Override
    public Entity findById(Identifier entityId) {
        return repository.findById(entityId).orElse(null);
    }


    @Override
    @Transactional
    public Entity findByUniqueKey(Map<String, Object> fieldValueMap) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Entity> cq = criteriaBuilder.createQuery(entityClass);
        Root<Entity> entityRoot = cq.from(entityClass);
        List<Predicate> filterPredicates = new ArrayList<>();
        fieldValueMap.forEach((key, value) -> filterPredicates.add(criteriaBuilder.equal(entityRoot.get(key), value)));
        cq.where(filterPredicates.toArray(new Predicate[0]));
        return (entityManager.createQuery(cq).getResultList().size() != 0
            && entityManager.createQuery(cq).getResultList().size()==1) ?
            entityManager.createQuery(cq).getResultList().get(0) : null;
    }

    @Override
    public Page<Entity> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public long count() {
        return repository.count();
    }

    @Override
    public long count(String query) {
        String simpleNameClass = getSimpleName();
        String aliasSimpleNameClass = getNameAlias();
        return entityManager.createQuery(
                SELECT
                        + aliasSimpleNameClass
                        + FROM
                        + simpleNameClass
                        + WHITE_SPACE
                        + aliasSimpleNameClass
                        + WHERE
                        + query
        ).getResultList().size();
    }

    @Override
    @Transactional
    public List<Entity> findByMultipleIds(List<Identifier> ids, boolean orderedReturn) {
        String simpleNameClass = getSimpleName();
        String aliasSimpleNameClass = getNameAlias();
        String query = SELECT
                + aliasSimpleNameClass
                + FROM
                + simpleNameClass
                + WHITE_SPACE
                + aliasSimpleNameClass
                + WHERE
                + aliasSimpleNameClass + "."
                + ID
                + IN + "("
                + StringUtils.join(ids, ",")
                + ")"
                + orderedReturnQuery(orderedReturn);
        return entityManager.createQuery(query).getResultList();
    }

    private String orderedReturnQuery(boolean orderedReturn) {
        return orderedReturn ? ORDER_BY + ID + " asc " : "";
    }

    @Override
    @Transactional
    public Page<Entity> search(QueryFilter[] queryFilters, Pageable pageable) {
        // TODO: Rewrite to use JPA Criteria as Hibernate Criteria deprecated since 5.2 for Session
        Criteria criteria = entityManager.unwrap(Session.class).createCriteria(entityClass);
        Set<String> aliases = new HashSet<>();
        if (ArrayUtils.isNotEmpty(queryFilters)) {
            for (QueryFilter queryFilter : queryFilters) {
                final String attributeName = queryFilter.getAttributeName();

                // if search filter contains related table property, then add entity alias to criteria to perform search on related properties.
                CriteriaUtils.criteriaForRelatedProperty(criteria, attributeName, aliases);

                Criterion criterion = CriteriaUtils.createCriterion(queryFilter);
                criteria.add(criterion);
            }
        }
        return CriteriaUtils.executeAndGetPageableData(criteria, pageable, aliases);
    }

    @Override
    public Page<Entity> searchByQuery(String query, Pageable pageable) {
        String simpleNameClass = getSimpleName();
        String aliasSimpleNameClass = getNameAlias();
        Query queryEntity = entityManager.createQuery(
                SELECT
                        + aliasSimpleNameClass
                        + FROM
                        + simpleNameClass
                        + WHITE_SPACE
                        + aliasSimpleNameClass
                        + WHERE
                        + query
        );

        List<Entity> entities;
        long total;

        if (nonNull(pageable)) {

            queryEntity
                    .setFirstResult((int) pageable.getOffset())
                    .setMaxResults(pageable.getPageSize());

            total = count(query);

            entities = total > pageable.getOffset() ? queryEntity.getResultList() : Collections.emptyList();

        } else {
            entities = queryEntity.getResultList();
        }

        return new PageImpl<>(entities, pageable, entities.size());
    }

    @Override
    @Transactional
    public Page<Map<String, Object>> getAggregatedValues(AggregationInfo aggregationInfo, Pageable pageable) {

        WMQueryInfo queryInfo = build(aggregationInfo);

        String query = queryInfo.getQuery();

        String countQuery = getCountQuery(query);

        long count = (Long) entityManager.createQuery(countQuery).getSingleResult();

        Query queryEntity = entityManager.createQuery(query);

        List<String> entities;

        if (nonNull(pageable)) {

            queryEntity
                    .setFirstResult((int) pageable.getOffset())
                    .setMaxResults(pageable.getPageSize());

            entities = count > pageable.getOffset() ? queryEntity.getResultList() : Collections.emptyList();

        } else {
            entities = Collections.emptyList();
        }

        System.out.println(entities);

        return null;
    }

    private Map<String, Object> getAliasValues(Query queryEntity) {
        String[] alias = ((QueryImpl) queryEntity).getReturnAliases();
        List<Integer> values = (List<Integer>) queryEntity.getResultList().get(0);

        Map<String, Object> dataAliases = new HashMap<>();

        for (int i = 0; i < alias.length; i++) {
            dataAliases.put(alias[i], values.get(i));
        }

        return dataAliases;

    }

    public WMQueryInfo build(AggregationInfo aggregationInfo) {
        StringBuilder builder = new StringBuilder();
        Map<String, Object> parameters = new HashMap<>();

        String projections = generateProjections(aggregationInfo);

        if (StringUtils.isNotBlank(projections)) {
            builder.append("select ")
                    .append(projections)
                    .append(" ");
        }

        builder.append("from ")
                .append(getSimpleName())
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

    private String generateProjections(AggregationInfo aggregationInfo) {
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

    private String cleanAlias(String alias) {
        return alias.replaceAll("\\.", "\\$");
    }

    private WMQueryInfo interceptFilter(String filter) {
        WMQueryInfo queryInfo = new WMQueryInfo(filter);

        for (final QueryInterceptor interceptor : interceptors) {
            interceptor.intercept(queryInfo);
        }

        return queryInfo;
    }

    private String getCountQuery(String query) {
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

    @Override
    @Transactional
    public <Entity> Entity execute(HibernateCallback<Entity> hibernateCallback) {
        HibernateTemplate template = new HibernateTemplate(entityManager.getEntityManagerFactory().unwrap(SessionFactory.class));
        return template.execute(hibernateCallback);
    }

    public JpaRepository<Entity, Identifier> getRepository() {
        return repository;
    }

    public void setRepository(
            JpaRepository<Entity, Identifier> repository) {
        this.repository = repository;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    private String getSimpleName() {
        return entityClass.getSimpleName();
    }

    private String getNameAlias() {
        return entityClass.getSimpleName().substring(0, 1).toLowerCase();
    }
}
