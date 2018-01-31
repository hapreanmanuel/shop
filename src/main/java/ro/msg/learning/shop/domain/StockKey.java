package ro.msg.learning.shop.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Embeddable
public class StockKey implements Serializable {
    @Column(name="PRODUCTID")
    private int productId;
    @Column(name="LOCATIONID")
    private int locationId;
}
