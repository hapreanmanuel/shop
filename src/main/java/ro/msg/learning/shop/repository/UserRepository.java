package ro.msg.learning.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.msg.learning.shop.domain.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);
}
