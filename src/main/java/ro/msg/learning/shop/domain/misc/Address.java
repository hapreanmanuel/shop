package ro.msg.learning.shop.domain.misc;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Data
@Embeddable
public class Address {
    @Column(name="ADDRESS")
    private String fullAddress;

    @Column(name="COUNTRY")
    private String country;

    @Column(name="CITY")
    private String city;

    @Column(name="REGION")
    private String region;
}
