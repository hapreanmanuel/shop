package ro.msg.learning.shop.domain;

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
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="SUPPLIERID")
    private int supplierId;                         //PK

    @Column(name="NAME", unique = true)
    private String name;
}

