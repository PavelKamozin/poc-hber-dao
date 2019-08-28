package softserve.hibernate.com.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Map;

public abstract class HibernateGenericDaoAbstract<Entity, Identifier> implements HibernateGenericDao<Entity, Identifier> {

    protected JpaRepository<Entity, Identifier> repository;

    public HibernateGenericDaoAbstract(JpaRepository<Entity, Identifier> repository) {
        this.repository = repository;
    }

    @Override
    public Entity create(Entity entity) {
        return null;
    }

    @Override
    public void update(Entity entity) {

    }

    @Override
    public void delete(Entity entity) {

    }

    @Override
    public Entity findById(Identifier entityId) {
        return null;
    }

    @Override
    public Entity findByUniqueKey(Map<String, Object> fieldValueMap) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public long count(String query) {
        return 0;
    }

}
