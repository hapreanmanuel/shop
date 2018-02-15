package ro.msg.learning.shop.domain;

import lombok.*;

import javax.persistence.*;

@NoArgsConstructor              //Needed by JPA
@Entity
@Data
@Table(name="Customer")
public class Customer {

    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int customerId;

    private String firstName;

    private String lastName;

    @Column(unique = true)
    private String userName;

    //Constructor based builder to exclude the option of including 'customerId' when building
    @Builder
    public Customer(String firstName, String lastName, String userName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
    }
}
