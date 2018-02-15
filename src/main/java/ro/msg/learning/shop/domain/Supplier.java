package ro.msg.learning.shop.domain;

import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@Entity
@Data
@Table(name="Supplier")
public class Supplier {

    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int supplierId;

    @Column(unique = true)
    private String name;

    @Builder
    public Supplier(String name) {
        this.name = name;
    }
}

