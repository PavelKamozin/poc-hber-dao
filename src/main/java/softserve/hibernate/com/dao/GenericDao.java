package softserve.hibernate.com.dao;

import com.wavemaker.runtime.data.expression.QueryFilter;
import com.wavemaker.runtime.data.model.AggregationInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.orm.hibernate5.HibernateCallback;

import java.util.List;
import java.util.Map;

public interface GenericDao<Entity, Identifier> {

    Entity create(Entity entity);

    void update(Entity entity);

    void delete(Entity entity);

    Entity findById(Identifier entityId);

    Entity findByUniqueKey(final Entity entity) throws IllegalAccessException;

    Page<Entity> list(Pageable pageable);

    List<Entity> findByMultipleIds(List<Identifier> ids, boolean orderedReturn);

    Page<Entity> search(QueryFilter queryFilters[], Pageable pageable);

    Page<Entity> searchByQuery(String query, Pageable pageable);

    long count();

    long count(String query);

    Page<Map<String, Object>> getAggregatedValues(final AggregationInfo aggregationInfo, Pageable pageable) throws IllegalAccessException;

    <Entity> Entity execute(HibernateCallback<Entity> hibernateCallback);

    Entity refresh(Entity entity);

    //Downloadable export(ExportType exportType, String query, Pageable pageable);
}
