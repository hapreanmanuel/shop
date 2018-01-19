package ro.msg.learning.shop.domain;

import lombok.Data;

@Data
public class Stock {

    int productId;                  //FK - Product: aggregate primary key

    int locationId;                 //FK - Location: aggregate primary key

    int quantity;
}
