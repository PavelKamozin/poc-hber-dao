package softserve.hibernate.com.dao.impl;

import com.wavemaker.runtime.data.expression.QueryFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softserve.hibernate.com.dao.GenericDaoAbstract;
import softserve.hibernate.com.entity.User;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class UserDaoImpl extends GenericDaoAbstract<User, Integer> {

    @Autowired
    public UserDaoImpl(EntityManager entityManager, JpaRepository<User, Integer> repository) {
        super(repository, entityManager);
    }

    public Page<User> search1(QueryFilter[] queryFilters, Pageable pageable) {

        User user = new User();

        ExampleMatcher firstNameMatcher = ExampleMatcher.matching()
                .withMatcher("name", m -> m.endsWith())
                //.withIgnorePaths("id");
                .withMatcher("id", m -> m.exact());
        user.setName("o");
        user.setId(1);

        Example<User> exampleQuery = Example.of(user, firstNameMatcher);

        List<User> all = getRepository().findAll(exampleQuery);
        PageImpl page = new PageImpl(all);

        return page;
    }

}
