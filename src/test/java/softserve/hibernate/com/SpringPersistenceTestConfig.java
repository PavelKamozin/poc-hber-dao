package softserve.hibernate.com;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.rest.RepositoryRestMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.ApplicationContext;

@SpringBootApplication(exclude = {
        WebMvcAutoConfiguration.class,
        SpringDataWebAutoConfiguration.class,
        RepositoryRestMvcAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class})
public class SpringPersistenceTestConfig {

    @Autowired
    private ApplicationContext context;

    @PostConstruct
    public void load() {

    }

    @PreDestroy
    public void deleteDB() {

    }

    public static void main(String[] args) {
        SpringApplication.run(SpringPersistenceTestConfig.class, args);
    }
}
