package softserve.hibernate.com;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import softserve.hibernate.com.dao.impl.RoleDaoImpl;
import softserve.hibernate.com.entity.Role;
import softserve.hibernate.com.repository.RoleRepository;

import java.util.logging.Logger;

@SpringBootApplication
public class Application implements CommandLineRunner {

    @Autowired
    private RoleDaoImpl roleDao;

    @Autowired
    private RoleRepository roleRepository;

    private static final Logger log = Logger.getLogger(Application.class.getName());

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }

    @Override
    public void run(String... args) {
        log.info("Hello world");

        roleRepository.deleteAll();

        Role role = new Role();
        role.setRoleType(RoleType.ADMIN);

        roleDao.create(role);

        Role role1 = new Role();
        role1.setRoleType(RoleType.USER);

        roleDao.create(role1);

        Role role2 = new Role();
        role2.setRoleType(RoleType.GUEST);

        roleDao.create(role2);

        System.out.println(roleDao.searchByQuery("role is not null", PageRequest.of(0 ,2)));

        roleRepository.deleteAll();
    }

}
