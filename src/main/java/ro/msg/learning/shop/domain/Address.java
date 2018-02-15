package ro.msg.learning.shop.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Data
@Embeddable
public class Address {

    @Column(name="ADDRESS")
    private String fullAddress = "";

    private String country = "";

    private String city = "";

    private String region = "";

}
