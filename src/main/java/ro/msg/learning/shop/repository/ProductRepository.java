package ro.msg.learning.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.msg.learning.shop.domain.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}
