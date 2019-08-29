package softserve.hibernate.com.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softserve.hibernate.com.constant.RoleType;
import softserve.hibernate.com.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    Role getByRoleType(RoleType roleType);
}
