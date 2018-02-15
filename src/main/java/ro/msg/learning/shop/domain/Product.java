package ro.msg.learning.shop.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@Table(name="Product")
public class Product {

    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int productId;

    @ManyToOne
    @JoinColumn(name="CATEGORYID")
    private ProductCategory category;

    @ManyToOne
    @JoinColumn(name="SUPPLIERID")
    private Supplier supplier;

    @Column(unique=true)
    private String name;

    @Builder.Default
    private String description = "";

    private BigDecimal price;

    private double weight;

}
