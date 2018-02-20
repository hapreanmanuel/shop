package ro.msg.learning.shop.domain;

import lombok.*;

import javax.persistence.*;

@NoArgsConstructor              //Needed by JPA
@Entity
@Data
@Table(name="Customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="CUSTOMERID")
    private int customerId;

 //   @JoinColumn(name="USER_ID", unique = true)
    @MapsId
    @OneToOne(cascade = CascadeType.ALL,orphanRemoval = true)
    private User user;

    private String firstName;

    private String lastName;

    @Builder
    public Customer(User user, String firstName, String lastName) {
        this.user = user;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
