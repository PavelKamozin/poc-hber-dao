package softserve.hibernate.com.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository<User, Identifier> extends JpaRepository {
}
