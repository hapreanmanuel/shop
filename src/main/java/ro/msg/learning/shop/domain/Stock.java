package ro.msg.learning.shop.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity(name="Stock")
@Table(name="STOCK")
public class Stock {

    @EmbeddedId
    private StockKey stockKey;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="PRODUCTID",referencedColumnName = "PRODUCTID",insertable = false,updatable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="LOCATIONID",referencedColumnName = "LOCATIONID",insertable = false,updatable = false)
    private Location location;

    @Column(name="QUANTITY")
    private int quantity;
}

@AllArgsConstructor
@NoArgsConstructor
@Data
@Embeddable
class StockKey implements Serializable {
    @Column(name="PRODUCTID")
    private int productId;
    @Column(name="LOCATIONID")
    private int locationId;
}
