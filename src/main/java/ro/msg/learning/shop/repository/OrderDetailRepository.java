package ro.msg.learning.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.msg.learning.shop.domain.OrderDetailKey;
import ro.msg.learning.shop.domain.OrderDetail;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, OrderDetailKey>{
}
