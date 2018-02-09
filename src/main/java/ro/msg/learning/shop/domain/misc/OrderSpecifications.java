package ro.msg.learning.shop.domain.misc;

/*
    This class is used as an argument for the OrderService method 'createNewOrder'
    It contains all the user-input fields (customerId, address and a list of desired products)
    as well as generated fields (timestamp)
 */

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@NoArgsConstructor
@Data
public class OrderSpecifications {

    private int customerId;

    private List<ShoppingCartEntry> shoppingCart;

    private Timestamp orderCreationTimestamp;

    private Address address;
}

