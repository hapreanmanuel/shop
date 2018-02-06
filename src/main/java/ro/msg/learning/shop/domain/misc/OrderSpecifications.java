package ro.msg.learning.shop.domain.misc;

/*
    This class is used as an argument for the OrderCreationService method 'createNewOrder'
    It contains all the user-input fields (customerId, address and a list of desired products)
    as well as generated fields (timestamp)

 */

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
public class OrderSpecifications {

    @Setter(AccessLevel.NONE)
    private final int customerId;

    @Setter(AccessLevel.NONE)
    private List<ShoppingCartEntry> shoppingCart;

    private final Timestamp orderCreationTimestamp;

    private Address address;

    //Constructor
    public OrderSpecifications(int customerId) {
        this.customerId = customerId;
        this.shoppingCart = new ArrayList<>();
        this.orderCreationTimestamp = new Timestamp(System.currentTimeMillis());
    }

    public void addShoppingCartEntry(ShoppingCartEntry shoppingCartEntry){
        shoppingCart.add(shoppingCartEntry);
    }

    public void removeShoppingCartEntry(ShoppingCartEntry shoppingCartEntry) {
        if(shoppingCart.contains(shoppingCartEntry)) shoppingCart.remove(shoppingCartEntry);
    }

}

