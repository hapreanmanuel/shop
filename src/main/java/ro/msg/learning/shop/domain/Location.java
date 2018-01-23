package ro.msg.learning.shop.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import javax.persistence.*;


@Entity(name="Location")
@Data
@Table(name="Location")
public class Location {

    @Setter(AccessLevel.NONE)
    @Id
    @Column(name="LOCATIONID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int locationId;

    @Column(name="NAME")
    private String name;

    @Column(name="COUNTRY")
    private String country;

    @Column(name="CITY")
    private String city;

    @Column(name="REGION")
    private String region;

    @Column(name="ADDRESS")
    private String address;

}
