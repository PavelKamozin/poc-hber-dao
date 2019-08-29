package softserve.hibernate.com.dao;

import com.wavemaker.runtime.data.expression.QueryFilter;
import com.wavemaker.runtime.data.model.AggregationInfo;
import com.wavemaker.runtime.data.util.CriteriaUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.Query;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.HashSet;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class GenericDaoAbstract<Entity extends Serializable, Identifier extends Serializable> implements GenericDao<Entity, Identifier> {

    private static final String SELECT = " select ";
    private static final String FROM = " from ";
    private static final String WHERE = " where ";
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
    public Entity findByUniqueKey(Map<String, Object> fieldValueMap) {
        return null;
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
    public List<Entity> findByMultipleIds(List<Identifier> ids, boolean orderedReturn) {
        return null;
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
        ).setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize());

        long total = count(query);

        List<Entity> entities = total > pageable.getOffset() ? queryEntity.getResultList() : Collections.emptyList();

        return new PageImpl<>(entities, pageable, total);
    }

    @Override
    public Page<Map<String, Object>> getAggregatedValues(AggregationInfo aggregationInfo, Pageable pageable) {
        return null;
    }

    public JpaRepository<Entity, Identifier> getRepository() {
        return repository;
    }

    public void setRepository(
        JpaRepository<Entity, Identifier> repository) {
        this.repository = repository;
    }

    private String getSimpleName() {
        return entityClass.getSimpleName();
    }

    private String getNameAlias() {
        return entityClass.getSimpleName().substring(0, 1).toLowerCase();
    }


    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
