package ro.msg.learning.shop.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity(name="Order")
@Data
@Table(name="order_table")                      //Changed table name from 'order' to 'order_table'
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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

    @Column(name="COUNTRY")
    private String country;

    @Column(name="CITY")
    private String city;

    @Column(name="REGION")
    private String region;

    @Column(name="ADDRESS")
    private String address;

}
