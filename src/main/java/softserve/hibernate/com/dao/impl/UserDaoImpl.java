package softserve.hibernate.com.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import softserve.hibernate.com.dao.GenericDaoAbstract;
import softserve.hibernate.com.entity.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class UserDaoImpl extends GenericDaoAbstract<User, Integer> {

    @Autowired
    public UserDaoImpl(JpaRepository<User, Integer> repository, EntityManager entityManager) {
        super(repository, entityManager);
    }

    void test() {
        getRepository().findById(1);
    }
}
