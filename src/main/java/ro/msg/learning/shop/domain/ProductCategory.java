package ro.msg.learning.shop.domain;

import lombok.Data;

@Data
public class ProductCategory {

    int categoryId;                     //PK

    String name;

    String description;

}
