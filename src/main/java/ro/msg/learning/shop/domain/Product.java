package ro.msg.learning.shop.domain;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Product {

    int productId;                      //primary key

    int categoryId;                     //foreign key (ProductCategory)

    int supplierId;                     //foreign key (Supplier)

    String name;

    String description;

    BigDecimal price;

    double weight;



}
