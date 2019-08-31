package softserve.hibernate.com.dao;

import com.wavemaker.runtime.data.expression.QueryFilter;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.orm.hibernate5.HibernateCallback;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.*;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static softserve.hibernate.com.builder.QueryBuilder.build;
import static softserve.hibernate.com.builder.QueryBuilder.configureParameters;
import static softserve.hibernate.com.builder.QueryBuilder.getCountQuery;

public abstract class GenericDaoAbstract<Entity extends Serializable, Identifier extends Serializable> implements GenericDao<Entity, Identifier> {

    private static final String SELECT = " select ";
    private static final String FROM = " from ";
    private static final String WHERE = " where ";
    private static final String IN = " in ";
    private static final String ID = "id ";
    private static final String ORDER_BY = " order by ";
    private static final String WHITE_SPACE = " ";

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
                && entityManager.createQuery(cq).getResultList().size() == 1) ?
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

        WMQueryInfo queryInfo = build(aggregationInfo, getSimpleName(), getNameAlias());

        String query = configureParameters(queryInfo.getQuery(), queryInfo.getParameters());

        String countQuery = getCountQuery(query);

        long count = isNull(countQuery) ? Integer.MAX_VALUE : (Long) entityManager.createQuery(countQuery).getSingleResult();

        List<Map<String, Object>> result = new ArrayList<>();

        List<Aggregation> aggregations = aggregationInfo.getAggregations();

        if (!aggregations.isEmpty()) {

            List<Tuple> aggregationData = entityManager.createQuery(query, Tuple.class).getResultList();

            Map<String, Object> data = new HashMap<>();

            if (!aggregationData.isEmpty()) {

                Tuple tuple = aggregationData.get(0);

                for (Aggregation aggregation : aggregations) {
                    data.put(aggregation.getAlias(), tuple.get(aggregation.getAlias()));
                }

            }

            result.add(data);
        } else {

            List<Entity> entityList;

            Query entityQuery = entityManager.createQuery(query);

            if (nonNull(pageable)) {
                entityQuery
                        .setFirstResult((int) pageable.getOffset())
                        .setMaxResults(pageable.getPageSize());

                entityList = count > pageable.getOffset() ? entityQuery.getResultList() : Collections.emptyList();
            } else {
                entityList = Collections.emptyList();
            }

            for (int i = 0; i < entityList.size(); i++) {
                Entity entity = entityList.get(i);
                Map<String, Object> item = new HashMap<>();
                item.put(String.valueOf(i), entity);
                result.add(item);
            }
        }

        return new PageImpl<>(result, pageable, count);
    }

    @Override
    public Entity refresh(Entity entity) {
        return repository.saveAndFlush(entity);
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
