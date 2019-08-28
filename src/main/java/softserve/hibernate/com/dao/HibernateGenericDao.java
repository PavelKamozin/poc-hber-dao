package softserve.hibernate.com.dao;

import java.util.Map;

public interface HibernateGenericDao<Entity, Identifier> {

    Entity create(Entity entity);

    void update(Entity entity);

    void delete(Entity entity);

    Entity findById(Identifier entityId);

    Entity findByUniqueKey(final Map<String, Object> fieldValueMap);

    //Page<Entity> list(Pageable pageable);

    //Page getAssociatedObjects(Object value, String entityName, String key, Pageable pageable);

    //Page<Entity> search(QueryFilter queryFilters[], Pageable pageable);

    //Page<Entity> searchByQuery(String query, Pageable pageable);

    long count();

    long count(String query);

    //Page<Map<String, Object>> getAggregatedValues(final AggregationInfo aggregationInfo, Pageable pageable);

    //Downloadable export(ExportType exportType, String query, Pageable pageable);
}
