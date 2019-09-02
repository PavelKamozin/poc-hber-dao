package softserve.hibernate.com;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.rest.RepositoryRestMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import softserve.hibernate.com.constant.RoleType;
import softserve.hibernate.com.entity.Role;
import softserve.hibernate.com.repository.RoleRepository;
import softserve.hibernate.com.repository.UserRepository;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@SpringBootApplication(exclude = {
        WebMvcAutoConfiguration.class,
        SpringDataWebAutoConfiguration.class,
        RepositoryRestMvcAutoConfiguration.class})
public class SpringPersistenceTestConfig {
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    public void load() {
        userRepository.deleteAll();
        roleRepository.deleteAll();
        roleRepository.save(new Role(RoleType.ADMIN));
        roleRepository.save(new Role(RoleType.USER));
        roleRepository.save(new Role(RoleType.GUEST));
    }

    @PreDestroy
    public void deleteTestData() {
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringPersistenceTestConfig.class, args);
    }
}
