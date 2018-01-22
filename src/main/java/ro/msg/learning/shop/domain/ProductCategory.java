package ro.msg.learning.shop.domain;

import lombok.Data;

import javax.persistence.*;

@Table(name="productcategroy")
@Entity
@Data
public class ProductCategory {

    @Id
    @Column(name="CATEGORYID")
    @Basic
    int categoryId;                     //PK

    @Column(name="NAME")
    @Basic
    String name;

    @Column(name="DESCRIPTION")
    @Basic
    String description;

}
