package ro.msg.learning.shop.domain;

import lombok.Data;

@Data
public class Order {

    int orderId;

    int locationId;             //FK - Location: products are shipped from here

    int customerId;             //FK - Customer: buyer

    String country;

    String city;

    String region;

    String address;

}
