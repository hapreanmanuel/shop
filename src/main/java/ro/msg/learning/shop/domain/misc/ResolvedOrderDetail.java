package ro.msg.learning.shop.domain.misc;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ResolvedOrderDetail {
    //Product, Location, Quantity
    private final int productId;
    private final int quantity;
    private final int locationId;
}