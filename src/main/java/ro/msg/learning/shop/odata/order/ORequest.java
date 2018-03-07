package ro.msg.learning.shop.odata.order;

import lombok.Data;
import ro.msg.learning.shop.domain.Address;
import ro.msg.learning.shop.dto.ShoppingCartEntry;

import java.util.List;

@Data
public class ORequest {
    private String username;
    private List<ShoppingCartEntry> shoppingCart;
    private Address address;
}
