package ro.msg.learning.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ShoppingCartEntry {
    private final int productId;
    private final int quantity;
}
