package ro.msg.learning.shop.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name="order_table")                      //Changed table name from 'order' to 'order_table'
public class Order {

    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ORDER_ID")
    private int orderId;

    @ManyToOne
    private Location location;

    @ManyToOne
    private Customer customer;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetail> orderDetails;

    @Embedded
    private Address shippingAddress;

    private boolean revenued = false;

    @Column(name="status", columnDefinition = "VARCHAR(255) default 'CREATED'")
    @Enumerated(EnumType.STRING)
    private Status status = Order.Status.CREATED;

    public enum Status{
        CREATED,
        PROCESSING,
        DENYED,
        COMPLETE
    }
}
