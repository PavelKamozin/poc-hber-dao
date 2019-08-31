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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Logger;

import static java.util.Arrays.asList;

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
    public void run(String... args) throws ParseException {
        log.info("Hello world");

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        roleRepository.deleteAll();

        Role role = new Role();
        role.setRoleType(RoleType.ADMIN);
        role.setCreated(formatter.parse("2000-01-01"));

        roleRepository.save(role);

        Role role1 = new Role();
        role1.setRoleType(RoleType.USER);
        role1.setCreated(formatter.parse("2000-01-02"));

        roleRepository.save(role1);

        Role role2 = new Role();
        role2.setRoleType(RoleType.GUEST);
        role2.setCreated(formatter.parse("2000-01-01"));

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

//        aggregationInfo.setAggregations(emptyList());

//        aggregationInfo.setGroupByFields(emptyList());

        aggregationInfo.setGroupByFields(new ArrayList<>(asList("roleType")));

        aggregationInfo.setFilter("created > wm_TS('915141600000') and created < wm_TS('978300000000')");

        Page<Map<String, Object>> page = roleDao.getAggregatedValues(aggregationInfo, PageRequest.of(0, 2));

        System.out.println(page);
    }
}
