package softserve.hibernate.com.dao.impl;

import java.util.logging.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import softserve.hibernate.com.Application;
import softserve.hibernate.com.PersistenceTestBase;

public class UserDaoImplTest extends PersistenceTestBase {

    private static final Logger log = Logger.getLogger(UserDaoImplTest.class.getName());

    @Autowired
    private UserDaoImpl dao;

    @Test
    public void test() {
        log.info(UserDaoImplTest.class.getName());
    }
}
