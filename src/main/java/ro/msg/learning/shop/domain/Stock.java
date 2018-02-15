package ro.msg.learning.shop.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Data
@Table(name="STOCK")
public class Stock {

    @EmbeddedId
    private StockKey stockKey;

    @ManyToOne
    @JoinColumn(name="PRODUCTID",referencedColumnName = "PRODUCTID",insertable = false,updatable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name="LOCATIONID",referencedColumnName = "LOCATIONID",insertable = false,updatable = false)
    private Location location;

    private int quantity;

}

