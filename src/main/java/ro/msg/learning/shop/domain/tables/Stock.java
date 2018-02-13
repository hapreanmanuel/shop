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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="PRODUCTID",referencedColumnName = "PRODUCTID",insertable = false,updatable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="LOCATIONID",referencedColumnName = "LOCATIONID",insertable = false,updatable = false)
    private Location location;

    @Column(name="QUANTITY")
    private int quantity;

    //Key field accessors
    public int getProductId(){
        return stockKey.getProductId();
    }
    public int getLocationId(){
        return stockKey.getLocationId();
    }
}

