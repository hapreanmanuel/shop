package ro.msg.learning.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.msg.learning.shop.domain.Location;

public interface LocationRepository extends JpaRepository<Location, Integer>{
}
