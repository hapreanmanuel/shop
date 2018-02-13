package ro.msg.learning.shop.domain.misc;

/*
    This class is used as an argument for the OrderService method 'createNewOrder'
    It contains all the user-input fields (customerId, address and a list of desired products)
    as well as generated fields (timestamp)
 */

import lombok.Data;
import java.sql.Timestamp;
import java.util.List;

@Data
public class OrderSpecifications {

    private int customerId;

    private List<ShoppingCartEntry> shoppingCart;

    private final Timestamp orderCreationTimestamp;

    private Address address;

    public OrderSpecifications(){
        orderCreationTimestamp = new Timestamp(System.currentTimeMillis());
    }
}

