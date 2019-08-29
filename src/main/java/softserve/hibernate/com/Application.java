package softserve.hibernate.com;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import softserve.hibernate.com.dao.impl.RoleDaoImpl;
import softserve.hibernate.com.dao.impl.UserDaoImpl;
import softserve.hibernate.com.repository.RoleRepository;

import java.util.logging.Logger;

@SpringBootApplication
public class Application implements CommandLineRunner {

    @Autowired
    private RoleDaoImpl roleDao;

    @Autowired
    private RoleRepository roleRepository;

    private static final Logger log = Logger.getLogger(Application.class.getName());

    @Autowired
    private UserDaoImpl userDao;

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }

    @Override
    public void run(String... args) {
        log.info("Hello world");
    }

}
