package ro.msg.learning.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.msg.learning.shop.domain.Authority;
import ro.msg.learning.shop.domain.AuthorityType;

public interface AuthorityRepository extends JpaRepository<Authority, Integer>{
    Authority findByAuthorityType(AuthorityType authorityType);
}
