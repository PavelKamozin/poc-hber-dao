package softserve.hibernate.com;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import softserve.hibernate.com.dao.impl.UserDaoImpl;
import softserve.hibernate.com.repository.RoleRepository;
import softserve.hibernate.com.repository.UserRepository;


@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = {SpringPersistenceTestConfig.class})
public abstract class PersistenceTestBase {
    @Autowired
    private UserDaoImpl userDao;

    @Autowired
    private UserDaoImpl roleDao;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    public UserDaoImpl getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDaoImpl userDao) {
        this.userDao = userDao;
    }

    public UserDaoImpl getRoleDao() {
        return roleDao;
    }

    public void setRoleDao(UserDaoImpl roleDao) {
        this.roleDao = roleDao;
    }

    @Autowired
    public RoleRepository getRoleRepository() {
        return roleRepository;
    }

    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Autowired
    public UserRepository getUserRepository() {
        return userRepository;
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
