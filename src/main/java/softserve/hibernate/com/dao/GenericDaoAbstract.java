package softserve.hibernate.com.dao;

import lombok.Getter;
import org.hibernate.boot.model.naming.Identifier;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.Entity;
import javax.persistence.EntityNotFoundException;
import java.util.Map;
import java.util.Optional;

@Getter
public abstract class GenericDaoAbstract implements GenericDao<Entity, Integer> {

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
    public Entity findById(Integer entityId) {
        Optional<Entity> byId = repository.findById(Identifier.toIdentifier(entityId.toString()));
        return byId.orElseThrow(EntityNotFoundException::new);
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
