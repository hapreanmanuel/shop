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
import ro.msg.learning.shop.domain.Customer;

import java.sql.Timestamp;
import java.util.List;

@NoArgsConstructor
@Data
public class OrderSpecifications {

    private Customer customer;

    private List<ShoppingCartEntry> shoppingCart;

    private final Timestamp orderCreationTimestamp = new Timestamp(System.currentTimeMillis());

    private Address address;

    @Builder
    public OrderSpecifications(OrderCreationDto request, Customer customer) {
        this.customer = customer;
        this.shoppingCart = request.getShoppingCart();
        this.address = request.getAddress();
    }
}

