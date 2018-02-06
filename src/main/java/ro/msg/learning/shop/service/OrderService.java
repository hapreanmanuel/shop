package ro.msg.learning.shop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.domain.misc.ResolvedOrderDetail;
import ro.msg.learning.shop.domain.tables.Order;
import ro.msg.learning.shop.domain.misc.OrderSpecifications;
import ro.msg.learning.shop.domain.tables.OrderDetail;
import ro.msg.learning.shop.repository.OrderRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ShopService shopService;

    public OrderService(OrderRepository orderRepository, ShopService shopService){this.orderRepository = orderRepository; this.shopService = shopService;}

    public Order getOrder(int orderId){return orderRepository.findOne(orderId);}

    public List<Order> getAllOrders(){return orderRepository.findAll();}

    public List<Order> getAllOrdesForCustomer(int customerId){return orderRepository.findAllByCustomer_CustomerId(customerId);}

    public void addOrder(Order order){ orderRepository.save(order);}


/*•	You get a single java object as input. This object will contain the order timestamp, the delivery address and a list of products (product ID and quantity) contained in the order.
        •	You return an Order entity if the operation was successful. If not, you throw an exception.
        •	The service has to select a strategy for finding from which locations should the products be taken. See the strategy design pattern. The strategy should be selected based on a spring boot configuration. The following initial strategy should be created:
        o	Single location: find a single location that has all the required products (with the required quantities) in stock. If there are more such locations, simply take the first one based on the ID.
        •	The service then runs the strategy, obtaining a list of objects with the following structure: location, product, quantity (= how many items of the given product are taken from the given location). If the strategy is unable to find a suitable set of locations, it should throw an exception.
        •	The stocks need to be updated by subtracting the shipped goods.
        •	Afterwards the order is persisted in the database and returned.

*/

    //The return entry is not saved to the database yet
    //TODO exception handling
    public Order createNewOrder(OrderSpecifications orderSpecifications) throws RuntimeException {
        Order newOrder = new Order();

        newOrder.setCustomer(shopService.getCustomer(orderSpecifications.getCustomerId()));
        newOrder.setShippingAddress(orderSpecifications.getAddress());
        newOrder.setOrderDetails(new ArrayList<>());

        orderSpecifications.getShoppingCart().forEach(shoppingCartEntry -> newOrder.getOrderDetails().add(new OrderDetail(newOrder,shopService.getProduct(shoppingCartEntry.getProductId()))));
        return newOrder;
    }
}






