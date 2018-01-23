package ro.msg.learning.shop.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import javax.persistence.*;

@Entity(name="ProductCategroy")
@Data
@Table(name="PRODUCTCATEGORY")
public class ProductCategory {

    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="CATEGORYID")
    private int categoryId;                     //PK

    @Column(name="NAME", unique = true)
    private String name;

    @Column(name="DESCRIPTION")
    private String description;

}
