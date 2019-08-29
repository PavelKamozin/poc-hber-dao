package softserve.hibernate.com;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import softserve.hibernate.com.constant.RoleType;
import softserve.hibernate.com.dao.impl.RoleDaoImpl;
import softserve.hibernate.com.dao.impl.UserDaoImpl;
import softserve.hibernate.com.entity.Role;

import java.util.logging.Logger;
import softserve.hibernate.com.entity.User;
import softserve.hibernate.com.repository.RoleRepository;
import softserve.hibernate.com.repository.UserRepository;

@SpringBootApplication
public class Application implements CommandLineRunner {

    @Autowired
    private RoleDaoImpl roleDao;

    @Autowired
    private UserDaoImpl userDao;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    private static final Logger log = Logger.getLogger(Application.class.getName());

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }

    @Override
    public void run(String... args) {
        log.info("Hello world");

        clearTables();
        Role role = new Role(RoleType.ADMIN);
        roleDao.create(role);
        Role role2 = new Role(RoleType.USER);
        roleDao.create(role2);

        User user = new User("Andrii", role);
        userDao.create(user);
         user = new User("Andrii Torzhkov", role);
        userDao.create(user);
         user = new User("Andrii Kozachenko", role2);
        userDao.create(user);


        Map<String, Object> map = new HashMap<>();
        //map.put("name", "Andrii");
        map.put("role",role);

        System.out.println(userDao.findByUniqueKey(map));

        clearTables();
    }

    private void clearTables() {
        roleRepository.deleteAll();
        userRepository.deleteAll();
    }

}
