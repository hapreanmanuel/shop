package ro.msg.learning.shop.domain.tables;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import ro.msg.learning.shop.domain.misc.Address;

import javax.persistence.*;
import java.util.List;

@Entity(name="Order")
@Data
@Table(name="order_table")                      //Changed table name from 'order' to 'order_table'
public class Order {

    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ORDERID")
    private int orderId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="LOCATIONID")
    private Location location;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="CUSTOMERID")
    private Customer customer;

    @OneToMany(mappedBy = "order")
    private List<OrderDetail> orderDetails;

    private Address shippingAddress;

}
