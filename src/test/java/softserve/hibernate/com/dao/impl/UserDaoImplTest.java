package softserve.hibernate.com.dao.impl;

import com.wavemaker.runtime.data.expression.AttributeType;
import com.wavemaker.runtime.data.expression.QueryFilter;
import com.wavemaker.runtime.data.expression.Type;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import softserve.hibernate.com.PersistenceTestBase;
import softserve.hibernate.com.constant.RoleType;
import softserve.hibernate.com.dao.GenericDao;
import softserve.hibernate.com.entity.Role;
import softserve.hibernate.com.entity.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import static com.wavemaker.runtime.data.util.QueryParserConstants.NOTNULL;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class UserDaoImplTest extends PersistenceTestBase {
    private static final Logger log = Logger.getLogger(UserDaoImplTest.class.getName());
    private final String ROLAN_34 = "Rolan";
    private final String MIHO_99 = "Miho";
    private final String MAGA_35 = "Maga";
    private final String SULIKO_30 = "Suliko";
    private final String DATE_33 = "Date";
    private final String VANO_20 = "Vano";
    private final String VATO_20 = "Vato";

    private Role adminRole;
    private Role userRole;
    private Role guestRole;

    @Before
    public void createRoles() {
        adminRole = getRoleRepository().getByRoleType(RoleType.ADMIN);
        userRole = getRoleRepository().getByRoleType(RoleType.USER);
        guestRole = getRoleRepository().getByRoleType(RoleType.GUEST);
    }

    @Test
    public void testReadCreate() {
        User expected = getUserRepository().save(new User("Read user", "Name", "Job", 20, adminRole));
        User actual = getDao().findById(expected.getId());

        assertUser(expected, actual);
    }

    @Test
    public void testUpdate() {
        User original = getUserRepository().save(new User("Read user", "Name", "Job", 20, adminRole));
        User expected = getDao().findById(original.getId());

        expected.setName("New name");
        expected.setAge(999);
        getDao().update(expected);

        User actual = getDao().findById(expected.getId());
        assertUser(expected, actual);
    }

    @Test
    public void testDelete() {
        User original = getUserRepository().save(new User("Read user", "Name", "Job", 20, adminRole));
        User actual = getDao().findById(original.getId());

        assertUser(original, actual);
        getDao().delete(actual);

        User expected = getDao().findById(actual.getId());
        assertNull(expected);
    }

    @Test
    public void testSearchByQueryFilter() {
        createUsers(adminRole, userRole, guestRole);

        QueryFilter[] queryFilters = new QueryFilter[5];
        QueryFilter q1 = new QueryFilter();
        q1.setAttributeName("name");
        q1.setAttributeValue("Va");
        q1.setAttributeType(AttributeType.STRING);
        q1.setFilterCondition(Type.STARTING_WITH);
        queryFilters[0] = q1;

        QueryFilter q2 = new QueryFilter();
        q2.setAttributeName("lastName");
        q2.setAttributeValue("o");
        q2.setAttributeType(AttributeType.STRING);
        q2.setFilterCondition(Type.ENDING_WITH);
        queryFilters[1] = q2;

        QueryFilter q3 = new QueryFilter();
        q3.setAttributeName("age");
        q3.setAttributeValue(Arrays.asList(20, 25, 30));
        q3.setAttributeType(AttributeType.INTEGER);
        q3.setFilterCondition(Type.IN);
        queryFilters[2] = q3;

        QueryFilter q4 = new QueryFilter();
        q4.setAttributeName("job");
        q4.setAttributeValue(NOTNULL);
        q4.setAttributeType(AttributeType.TEXT);
        q4.setFilterCondition(Type.IS);
        queryFilters[3] = q4;

        QueryFilter q5 = new QueryFilter();
        q5.setAttributeName("role");
        q5.setAttributeValue(userRole);
        q5.setAttributeType(AttributeType.STRING);
        q5.setFilterCondition(Type.NOT_EQUALS);
        queryFilters[4] = q5;

        Pageable pageable = PageRequest.of(0, 20);
        Page<User> search = getDao().search(queryFilters, pageable);

        assertThat(search.getTotalElements(), is(2L));
        assertTrue(search.getContent().stream().allMatch(result ->
                result.getName().equals(VANO_20)
                        || result.getName().equals(VATO_20)));
    }

    @Test
    public void listCountTest() {
        Pageable pageable = PageRequest.of(2, 20);
        List<User> users = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            users.add(new User(UUID.randomUUID().toString(), adminRole));
        }
        getUserRepository().saveAll(users);

        Page<User> list = getDao().list(pageable);

        assertThat(list.getTotalElements(), is(100L));
        assertThat(list.getTotalPages(), is(5));
        assertThat(list.getContent().size(), is(20));

        long count = getDao().count();
        assertThat(count, is(100L));
    }

    @Test
    public void searchByQueryTest_number() {
        createUsers(adminRole, userRole, guestRole);
        Pageable pageable = PageRequest.of(0, 20);

        Page<User> users = getDao().searchByQuery("age > 34", pageable);
        assertThat(users.getTotalElements(), is(2L));
        assertTrue(users.getContent().stream().allMatch(result ->
                result.getName().equals(MAGA_35)
                        || result.getName().equals(MIHO_99)));
    }

    @Test
    public void searchByQueryTest_string() {
        createUsers(adminRole, userRole, guestRole);
        Pageable pageable = PageRequest.of(0, 20);

        Page<User> users = getDao().searchByQuery("name like 'Va%' and job is not null", pageable);
        assertThat(users.getTotalElements(), is(2L));
        assertTrue(users.getContent().stream().allMatch(result ->
                result.getName().equals(VANO_20)
                        || result.getName().equals(VATO_20)));
    }

    @Test
    public void countByQueryTest() {
        createUsers(adminRole, userRole, guestRole);

        long count = getDao().count("name like 'Va%' and job is not null");
        assertThat(count, is(2L));
    }

    @Test
    public void testFindByUniqueKeyWhenExist() {
        createUsers(adminRole, userRole, guestRole);
        User expectedUser = new User("IVano", "IAdzo", "policeman", 24, adminRole);
        getUserRepository().save(expectedUser);
        Map<String, Object> uniqueKey = new HashMap<>();
        uniqueKey.put("name", "IVano");
        uniqueKey.put("lastName", "IAdzo");
        uniqueKey.put("job", "policeman");
        uniqueKey.put("age", 24);
        uniqueKey.put("role", adminRole);
        User actualUser = getDao().findByUniqueKey(uniqueKey);
        assertUser(expectedUser, actualUser);
    }

    @Test
    public void testFindByUniqueKeyWhenNotExist() {
        createUsers(adminRole, userRole, guestRole);
        User expectedUser = new User("IVano", "IAdzo", "policeman", 24, adminRole);
        getUserRepository().save(expectedUser);
        Map<String, Object> uniqueKey = new HashMap<>();
        uniqueKey.put("name", "Iano");
        uniqueKey.put("lastName", "IAdzo");
        uniqueKey.put("job", "policeman");
        uniqueKey.put("age", 24);
        uniqueKey.put("role", adminRole);
        User actualUser = getDao().findByUniqueKey(uniqueKey);
        assertNull(actualUser);
    }

    public GenericDao<User, Integer> getDao() {
        return getUserDao();
    }

    private void assertUser(User expected, User actual) {
        assertNotNull(expected);
        assertNotNull(actual);
        assertThat(expected.getName(), is(actual.getName()));
        assertThat(expected.getAge(), is(actual.getAge()));
        assertThat(expected.getId(), is(actual.getId()));
        assertThat(expected.getJob(), is(actual.getJob()));
        assertThat(expected.getRole().getId(), is(actual.getRole().getId()));
    }

    private void createUsers(Role adminRole, Role userRole, Role guestRole) {
        getUserRepository().save(new User(VANO_20, "Adzo", "policeman", 20, adminRole));
        getUserRepository().save(new User(VANO_20, "Kadzo", null, 20, adminRole));
        getUserRepository().save(new User(VATO_20, "Idzo", "architect", 25, adminRole));
        getUserRepository().save(new User(SULIKO_30, "Shvili", "no job", 30, adminRole));
        getUserRepository().save(new User(MAGA_35, "Onodze", "valet", 35, userRole));
        getUserRepository().save(new User(DATE_33, "Redodze", "builder", 33, userRole));
        getUserRepository().save(new User(ROLAN_34, "Undodze", "doctor", 34, userRole));
        getUserRepository().save(new User(MIHO_99, "Lokodze", "narrator", 99, guestRole));
    }

    @Test
    public void testFindByMultipleIdsExist() {
        List<Integer> ids = new ArrayList<>();
        List<User> expectedUsers = new ArrayList<>();
        User expectedUser1 = getUserRepository().save(new User("Vato", "Idzo", "architect", 25, adminRole));
        expectedUsers.add(expectedUser1);
        User expectedUser2 = getUserRepository().save(new User("Suliko", "Shvili", "no job", 30, adminRole));
        expectedUsers.add(expectedUser2);
        ids.add(expectedUser1.getId());
        ids.add(expectedUser2.getId());

        getUserRepository().save(new User("Maga", "Onodze", "valet", 35, userRole));
        getUserRepository().save(new User("Date", "Redodze", "builder", 33, userRole));
        getUserRepository().save(new User("Rolan", "Undodze", "doctor", 34, userRole));
        List<User> actualUsers = getDao().findByMultipleIds(ids, false);

        assertEquals(actualUsers.size(), ids.size());
        assertArrayEquals(expectedUsers.toArray(), actualUsers.toArray());
    }
}
