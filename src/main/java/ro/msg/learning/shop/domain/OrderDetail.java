package ro.msg.learning.shop.domain;

import lombok.Data;

@Data
public class OrderDetail {

    int orderId;                       //FK - Order: aggregate primary key

    int productId;                     //FK - Product: aggregate primary key

    int quantity;

}
