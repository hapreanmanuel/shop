package ro.msg.learning.shop.domain;

import lombok.Data;

@Data
public class Location {

    int locationId;

    String name;

    String country;

    String city;

    String region;

    String address;

}
