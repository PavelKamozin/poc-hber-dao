package softserve.hibernate.com.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import softserve.hibernate.com.dao.GenericDaoAbstract;
import softserve.hibernate.com.entity.User;

@Service
public class UserDaoImpl extends GenericDaoAbstract<User, Integer> {

    @Autowired
    public UserDaoImpl(JpaRepository<User, Integer> repository) {
        super(repository);
    }

    void test() {
        getRepository().findById(1);
    }
}
