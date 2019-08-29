package softserve.hibernate.com.dao;

import com.wavemaker.runtime.data.expression.QueryFilter;
import com.wavemaker.runtime.data.model.AggregationInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;

public abstract class GenericDaoAbstract<Entity extends Serializable, Identifier extends Serializable> implements GenericDao<Entity, Identifier> {

    private static final String SELECT = " select ";
    private static final String FROM = " from ";
    private static final String WHERE = " where ";
    private static final String WHITE_SPACE = " ";

    private EntityManager entityManager;
    private Class<Entity> entityClass;
    private JpaRepository<Entity, Identifier> repository;

    public GenericDaoAbstract(JpaRepository<Entity, Identifier> repository, EntityManager entityManager) {
        this.repository = repository;
        this.entityManager = entityManager;

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
    public Page<Entity> search(QueryFilter[] queryFilters, Pageable pageable) {
        return null;
    }

    @Override
    public Page<Entity> searchByQuery(String query, Pageable pageable) {
        return null;
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

}
