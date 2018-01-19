package ro.msg.learning.shop.domain;

import lombok.Data;


@Data
public class Customer {

    int customerId;                         //primary key

    String firstName;

    String lastName;

    String userName;

}
