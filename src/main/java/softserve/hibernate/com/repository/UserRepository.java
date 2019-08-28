package softserve.hibernate.com.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softserve.hibernate.com.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
}
