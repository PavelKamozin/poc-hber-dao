package softserve.hibernate.com.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import softserve.hibernate.com.dao.GenericDaoAbstract;
import softserve.hibernate.com.entity.Role;
import softserve.hibernate.com.entity.User;

@Service
public class RoleDaoImpl extends GenericDaoAbstract<Role, Integer> {

    @Autowired
    public RoleDaoImpl(JpaRepository<Role, Integer> repository) {
        super(repository);
    }

}
