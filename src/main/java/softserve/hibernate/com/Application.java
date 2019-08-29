package softserve.hibernate.com;

import com.wavemaker.runtime.data.model.Aggregation;
import com.wavemaker.runtime.data.model.AggregationInfo;
import com.wavemaker.runtime.data.model.AggregationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import softserve.hibernate.com.constant.RoleType;
import softserve.hibernate.com.dao.impl.RoleDaoImpl;
import softserve.hibernate.com.entity.Role;
import softserve.hibernate.com.repository.RoleRepository;

import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Logger;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

@SpringBootApplication
public class Application implements CommandLineRunner {
    private static final Logger log = Logger.getLogger(Application.class.getName());

    @Autowired
    private RoleDaoImpl roleDao;

    @Autowired
    private RoleRepository roleRepository;

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }

    @Override
    public void run(String... args) {
        log.info("Hello world");

        roleRepository.deleteAll();

        Role role = new Role();
        role.setRoleType(RoleType.ADMIN);

        roleRepository.save(role);

        Role role1 = new Role();
        role1.setRoleType(RoleType.USER);

        roleRepository.save(role1);

        Role role2 = new Role();
        role2.setRoleType(RoleType.GUEST);

        roleRepository.save(role2);

        AggregationInfo aggregationInfo = new AggregationInfo();

        Aggregation aggregation = new Aggregation();
        aggregation.setField("id");
        aggregation.setType(AggregationType.SUM);
        aggregation.setAlias("sum");

        Aggregation aggregation1 = new Aggregation();
        aggregation1.setField("id");
        aggregation1.setType(AggregationType.MIN);
        aggregation1.setAlias("min");

        aggregationInfo.setAggregations(new ArrayList<>(asList(aggregation, aggregation1)));

//        aggregationInfo.setGroupByFields(new ArrayList<>(asList("role")));

        aggregationInfo.setGroupByFields(emptyList());
        aggregationInfo.setFilter("role is not null");

        Page<Map<String, Object>> page = roleDao.getAggregatedValues(aggregationInfo, PageRequest.of(0, 2));

        System.out.println(page);
    }
}
