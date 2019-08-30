package softserve.hibernate.com.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softserve.hibernate.com.dao.GenericDaoAbstract;
import softserve.hibernate.com.entity.User;

import javax.persistence.EntityManager;

@Repository
@Qualifier("UserDao")
public class UserDaoImpl extends GenericDaoAbstract<User, Integer> {

    @Autowired
    public UserDaoImpl(EntityManager entityManager, JpaRepository<User, Integer> repository) {
        super(repository, entityManager);
    }
}
