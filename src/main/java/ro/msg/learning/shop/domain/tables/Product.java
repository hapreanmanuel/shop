package ro.msg.learning.shop.domain.tables;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity(name="Product")
@Data
@Table(name="Product")
public class Product {

    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="PRODUCTID")
    private int productId;                      //primary key

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="CATEGORYID")
    private ProductCategory category;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="SUPPLIERID")
    private Supplier supplier;

    @Column(name="NAME",unique=true)
    private String name;

    @Column(name="DESCRIPTION")
    private String description;

    @Column(name="PRICE")
    private BigDecimal price;

    @Column(name="WEIGHT")
    private double weight;
}
