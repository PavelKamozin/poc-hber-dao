package softserve.hibernate.com.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.wavemaker.runtime.data.expression.QueryFilter;
import com.wavemaker.runtime.data.model.AggregationInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


public abstract class GenericDaoAbstract<Entity extends Serializable, Identifier extends Serializable> implements GenericDao<Entity, Identifier> {

    @PersistenceContext
    private EntityManager entityManager;

    private Class<Entity> entityClass;
    private JpaRepository<Entity, Identifier> repository;

    public GenericDaoAbstract(JpaRepository<Entity, Identifier> repository) {
        this.repository = repository;
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
        return entityManager.createQuery("select * from " + entityClass.getName() + " " + query).getResultList().size();
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
}
