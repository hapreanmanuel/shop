package ro.msg.learning.shop.domain.tables;

import lombok.Data;
import lombok.NoArgsConstructor;
import ro.msg.learning.shop.domain.misc.StockKey;

import javax.persistence.*;

@NoArgsConstructor
@Data
@Entity(name="Stock")
@Table(name="STOCK")
public class Stock {

    @EmbeddedId
    private StockKey stockKey;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="PRODUCTID",referencedColumnName = "PRODUCTID",insertable = false,updatable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="LOCATIONID",referencedColumnName = "LOCATIONID",insertable = false,updatable = false)
    private Location location;

    @Column(name="QUANTITY")
    private int quantity;

    //Constructor for stocks
    public Stock(Location location, Product product){
        this.location = location;
        this.product = product;
        this.quantity = 0;
        this.stockKey = new StockKey(product.getProductId(), location.getLocationId());
    }
}

