package ro.msg.learning.shop.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ro.msg.learning.shop.domain.Address;

import java.util.List;

@NoArgsConstructor
@Data
public class OrderCreationDto {
    private int customerId;

    private List<ShoppingCartEntry> shoppingCart;

    private Address address;

    public OrderCreationDto(int customerId) {
        this.customerId = customerId;
    }
}
