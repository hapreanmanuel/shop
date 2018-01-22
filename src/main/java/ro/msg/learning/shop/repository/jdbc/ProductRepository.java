package ro.msg.learning.shop.repository.jdbc;

import org.springframework.data.repository.CrudRepository;
import ro.msg.learning.shop.domain.Product;

public interface ProductRepository extends CrudRepository<Product, Integer>{
}
