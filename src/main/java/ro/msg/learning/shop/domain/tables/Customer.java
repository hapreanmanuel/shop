package ro.msg.learning.shop.domain.tables;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import javax.persistence.*;

@Entity(name="Customer")
@Data
@Table(name="Customer")
public class Customer {

    @Setter(AccessLevel.NONE)
    @Id
    @Column(name="CUSTOMERID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int customerId;                         //primary key

    @Column(name="FIRSTNAME")
    private String firstName;

    @Column(name="LASTNAME")
    private String lastName;

    @Column(name="USERNAME", unique = true)
    private String userName;

}
