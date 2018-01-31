package ro.msg.learning.shop.domain.tables;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import javax.persistence.*;

@Entity(name="Supplier")
@Data
@Table(name="Supplier")
public class Supplier {

    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="SUPPLIERID")
    private int supplierId;                         //PK

    @Column(name="NAME", unique = true)
    private String name;
}

