package ro.msg.learning.shop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Entity
@ToString(exclude = {"order", "product"})
@NoArgsConstructor
@Data
@Table(name="ORDER_DETAIL")
public class OrderDetail {

    @EmbeddedId
    private OrderDetailKey orderDetailKey;

    private int quantity;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="ORDERID",referencedColumnName = "ORDERID",insertable = false,updatable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name="PRODUCTID",referencedColumnName = "PRODUCTID",insertable = false,updatable = false)
    private Product product;

    public OrderDetail(OrderDetailKey orderDetailKey){
        this.orderDetailKey=orderDetailKey;
    }
}

