package ro.msg.learning.shop.domain;

import lombok.*;
import javax.persistence.*;

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

    //Constructor used when creating a new order detail
    public OrderDetail(Order order, Product product) {
        this.order = order;
        this.product = product;
        this.orderDetailKey = new OrderDetailKey(order.getOrderId(),product.getProductId());
    }
}


