package ro.msg.learning.shop.dto;

/*
    This class is used as an argument for the OrderService method 'createNewOrder'
    It contains all the user-input fields (customerId, address and a list of desired products)
    as well as generated fields (timestamp)
 */

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ro.msg.learning.shop.domain.Address;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Data
public class OrderSpecifications {

    private int customerId;

    private List<ShoppingCartEntry> shoppingCart;

    private List<ResolvedOrderDetail> resolution = new ArrayList<>();

    private final Timestamp orderCreationTimestamp = new Timestamp(System.currentTimeMillis());

    private Address address;

    @Builder
    public OrderSpecifications(int customerId, List<ShoppingCartEntry> shoppingCart, Address address) {
        this.customerId = customerId;
        this.shoppingCart = shoppingCart;
        this.address = address;
    }
}

