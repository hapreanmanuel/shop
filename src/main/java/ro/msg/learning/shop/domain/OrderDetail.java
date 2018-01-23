package ro.msg.learning.shop.domain;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name="OrderDetail")
@Data
@Table(name="ORDER_DETAIL")
public class OrderDetail {

    @EmbeddedId
    private OrderDetailKey orderDetailKey;

    @Column(name="QUANTITY")
    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="ORDERID",referencedColumnName = "ORDERID",insertable = false,updatable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="PRODUCTID",referencedColumnName = "PRODUCTID",insertable = false,updatable = false)
    private Product product;

}

@AllArgsConstructor
@NoArgsConstructor
@Data
@Embeddable
class OrderDetailKey implements Serializable{
    @Column(name="ORDERID")
    private int orderId;
    @Column(name="PRODUCTID")
    private int productId;
}
