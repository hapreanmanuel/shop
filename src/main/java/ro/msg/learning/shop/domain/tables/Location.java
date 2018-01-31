package ro.msg.learning.shop.domain.tables;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import ro.msg.learning.shop.domain.misc.Address;

import javax.persistence.*;


@Entity(name="Location")
@Data
@Table(name="Location")
public class Location {

    @Setter(AccessLevel.NONE)
    @Id
    @Column(name="LOCATIONID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int locationId;

    @Column(name="NAME", unique=true)
    private String name;

    private Address address;

}
