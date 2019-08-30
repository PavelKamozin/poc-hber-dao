package softserve.hibernate.com.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softserve.hibernate.com.dao.GenericDaoAbstract;
import softserve.hibernate.com.entity.Role;

import javax.persistence.EntityManager;

@Repository
@Qualifier("RoleDao")
public class RoleDaoImpl extends GenericDaoAbstract<Role, Integer> {

    @Autowired
    public RoleDaoImpl(JpaRepository<Role, Integer> repository, EntityManager entityManager) {
        super(repository, entityManager);
    }

}
