package ro.msg.learning.shop.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
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

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ManyToOne
    @JoinColumn(name="PRODUCTID",referencedColumnName = "PRODUCTID",insertable = false,updatable = false)
    private Product product;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ManyToOne
    @JoinColumn(name="LOCATIONID",referencedColumnName = "LOCATIONID",insertable = false,updatable = false)
    private Location location;

    private int quantity;

}

