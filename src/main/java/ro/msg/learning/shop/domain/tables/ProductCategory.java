package ro.msg.learning.shop.domain.tables;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="CATEGORYID")
    private int categoryId;                     //PK

    @Column(name="NAME", unique = true)
    private String name;

    @Column(name="DESCRIPTION")
    private String description;

}
