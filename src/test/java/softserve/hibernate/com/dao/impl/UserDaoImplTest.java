package softserve.hibernate.com.dao.impl;

import com.wavemaker.runtime.data.expression.AttributeType;
import com.wavemaker.runtime.data.expression.QueryFilter;
import com.wavemaker.runtime.data.expression.Type;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import softserve.hibernate.com.PersistenceTestBase;
import softserve.hibernate.com.constant.RoleType;
import softserve.hibernate.com.dao.GenericDao;
import softserve.hibernate.com.entity.Role;
import softserve.hibernate.com.entity.User;

import java.util.logging.Logger;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;

public class UserDaoImplTest extends PersistenceTestBase {

    private static final Logger log = Logger.getLogger(UserDaoImplTest.class.getName());
    public static final Role ROLE = new Role(RoleType.ADMIN);


    @Test
    public void testSearchByQueryFilter_startsWith() {
        final String VANO = "Vano";
        final String VATO = "Vato";

        Role byAdminRole = getRoleRepository().getByRoleType(RoleType.ADMIN);

        getUserRepository().save(new User("Vano", byAdminRole));
        getUserRepository().save(new User("Vato", byAdminRole));

        QueryFilter[] queryFilters = new QueryFilter[1];
        QueryFilter q1 = new QueryFilter();
        q1.setAttributeName("name");
        q1.setAttributeValue("Va");
        q1.setAttributeType(AttributeType.STRING);
        q1.setFilterCondition(Type.STARTING_WITH);
        queryFilters[0] = q1;

        Pageable pageable = PageRequest.of(0, 20);
        Page<User> search = getDao().search(queryFilters, pageable);

        assertThat(search.getTotalElements(), is(2L));
        assertTrue(search.getContent().stream().allMatch(result ->
                result.getName().equals(VANO)
                || result.getName().equals(VATO)));
    }

    public GenericDao<User, Integer> getDao() {
        return getUserDao();
    }
}
