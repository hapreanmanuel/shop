package ro.msg.learning.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.msg.learning.shop.domain.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    //Find all orders from specified location

    //Find all orders for specified customer

}
