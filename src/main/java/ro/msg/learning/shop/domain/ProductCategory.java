package ro.msg.learning.shop.domain;

import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@Entity
@Data
@Table(name="PRODUCTCATEGORY")
public class ProductCategory {

    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int categoryId;

    @Column(unique = true)
    private String name;

    private String description;

    @Builder
    public ProductCategory(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
