package softserve.hibernate.com.repository;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import softserve.hibernate.com.util.HibernateUtil;

@Repository
public class RoleRepository {

    public Role findById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().getCurrentSession()) {
            return session.get(Role.class, id);
        }
    }

}
