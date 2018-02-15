package ro.msg.learning.shop.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
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

