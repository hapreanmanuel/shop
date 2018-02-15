package ro.msg.learning.shop.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Embeddable
public class OrderDetailKey implements Serializable {
    @Column(name="ORDERID")
    private int orderId;
    @Column(name="PRODUCTID")
    private int productId;
}