package softserve.hibernate.com.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import softserve.hibernate.com.PersistenceTestBase;
import softserve.hibernate.com.entity.User;

@Slf4j
public class UserDaoImplTest extends PersistenceTestBase {
    @Autowired
    private UserDaoImpl dao;

    @Test
    public void test() {
        log.info(UserDaoImplTest.class.getName());
    }
}
