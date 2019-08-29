package softserve.hibernate.com.dao.impl;

import softserve.hibernate.com.PersistenceTestBase;
import softserve.hibernate.com.dao.GenericDao;
import softserve.hibernate.com.entity.User;

public class RoleDaoImplTest  extends PersistenceTestBase {

    public GenericDao<User, Integer> getGetDao() {
        return getRoleDao();
    }
}
