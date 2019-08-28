package softserve.hibernate.com.dao.impl;

import org.hibernate.boot.model.naming.Identifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import softserve.hibernate.com.dao.GenericDaoAbstract;

import javax.persistence.Entity;

@Service
public class UserDaoImpl<User, Integer> extends GenericDaoAbstract {

    public UserDaoImpl(JpaRepository<Entity, Identifier> repository) {
        super(repository);
    }
}
