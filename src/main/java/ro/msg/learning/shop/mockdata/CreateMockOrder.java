package ro.msg.learning.shop.mockdata;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import ro.msg.learning.shop.domain.Address;
import ro.msg.learning.shop.domain.Customer;
import ro.msg.learning.shop.dto.OrderSpecifications;
import ro.msg.learning.shop.dto.ShoppingCartEntry;
import ro.msg.learning.shop.repository.CustomerRepository;
import ro.msg.learning.shop.service.ShopService;

/*
     Create a mock order using the 'OrderService' class

     - customer: 1
     - wishlist:
        product 3, quantity 10
        product 4, quantity 20

     Expectations:
        The order is persisted in the 'Orders' table
        Orderdetails are persisted in 'Orderdetails' table
        Stocks are updated
 */
@Component
public class CreateMockOrder implements CommandLineRunner, Ordered {

    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE;
    }

    private final ShopService shopService;
    private final CustomerRepository customerRepository;

    @Autowired
    public CreateMockOrder(ShopService shopService, CustomerRepository customerRepository) {
        this.shopService = shopService;
        this.customerRepository = customerRepository;
    }



    @Override
    public void run(String... args) {

        final Customer customer = customerRepository.findOne(1);

        //Create an order for customer c1
        OrderSpecifications orderSpecifications = shopService.createBasicOrderSpecificationsForCustomer(customer.getCustomerId());

        orderSpecifications.setAddress(Address.builder()
                .fullAddress("Str. Dorobantilor 112B AP6")
                .city("Cluj-Napoca")
                .country("Romania")
                .region("CJ")
                .build());

        //Add some products to shopping cart
        orderSpecifications.getShoppingCart().add(new ShoppingCartEntry(3, 10));
        orderSpecifications.getShoppingCart().add(new ShoppingCartEntry(4,20));

        //Submit the order by calling the order creation method from orderService
        shopService.createNewOrder(orderSpecifications);
    }


}
