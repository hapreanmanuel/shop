package ro.msg.learning.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.msg.learning.shop.domain.OrderDetailKey;
import ro.msg.learning.shop.domain.OrderDetail;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, OrderDetailKey>{
    List<OrderDetail> findByOrderDetailKey_OrderId(int orderId);
}
