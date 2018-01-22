package ro.msg.learning.shop.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name="SUPPLIER")
@Data
public class Supplier {

    @Id
    @Column(name="SUPPLIERID")
    @Basic
    int supplierId;                         //PK

    @Column(name="name")
    @Basic
    String name;
}
