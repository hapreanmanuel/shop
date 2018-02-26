package ro.msg.learning.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.msg.learning.shop.domain.Supplier;

public interface SupplierRepository extends JpaRepository<Supplier, Integer>{
}
