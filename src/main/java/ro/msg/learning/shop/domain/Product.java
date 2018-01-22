package ro.msg.learning.shop.domain;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
@Table(name="product")
public class Product {

    @Id
    @Column(name="PRODUCTID")
    @Basic
    int productId;                      //primary key

    @Basic
    @Column(name="CATEGORYID")
    int categoryId;                     //foreign key (ProductCategory)

    @Basic
    @Column(name="SUPPLIERID")
    int supplierId;                     //foreign key (Supplier)

    @Basic
    @Column(name="NAME")
    String name;

    @Basic
    @Column(name="DESCRIPTION")
    String description;

    @Basic
    @Column(name="PRICE")
    BigDecimal price;

    @Basic
    @Column(name="WEIGHT")
    double weight;

}
