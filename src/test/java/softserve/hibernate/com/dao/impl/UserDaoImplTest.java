package softserve.hibernate.com.dao.impl;

import org.junit.Test;
import softserve.hibernate.com.PersistenceTestBase;

import java.util.logging.Logger;

public class UserDaoImplTest extends PersistenceTestBase {

    private static final Logger log = Logger.getLogger(UserDaoImplTest.class.getName());

    @Test
    public void test() {
        System.out.println(getRoleRepository().getClass());
        System.out.println(getUserRepository().getClass());
        log.info(UserDaoImplTest.class.getName());
    }
}
