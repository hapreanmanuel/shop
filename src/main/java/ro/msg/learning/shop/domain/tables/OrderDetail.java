package ro.msg.learning.shop.domain.tables;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import ro.msg.learning.shop.domain.misc.OrderDetailKey;

import javax.persistence.*;

@NoArgsConstructor
@Entity(name="OrderDetail")
@Data
@Table(name="ORDER_DETAIL")
public class OrderDetail {

    @EmbeddedId
    private OrderDetailKey orderDetailKey;

    @Column(name="QUANTITY")
    private int quantity;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="ORDERID",referencedColumnName = "ORDERID",insertable = false,updatable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="PRODUCTID",referencedColumnName = "PRODUCTID",insertable = false,updatable = false)
    private Product product;

    public OrderDetail(OrderDetailKey orderDetailKey){
        this.orderDetailKey=orderDetailKey;
    }

    @Override
    public String toString() {
        return "OrderDetail{" +
                "orderDetailKey=" + orderDetailKey +
                ", quantity=" + quantity +
                '}';
    }
}


