package ro.msg.learning.shop.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.domain.*;
import ro.msg.learning.shop.dto.OrderCreationDto;
import ro.msg.learning.shop.dto.OrderSpecifications;
import ro.msg.learning.shop.dto.ShoppingCartEntry;
import ro.msg.learning.shop.exception.EmptyShoppingCartException;
import ro.msg.learning.shop.exception.InvalidShippmentAddressException;
import ro.msg.learning.shop.repository.*;
import ro.msg.learning.shop.utility.distance.DistanceCalculator;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

/*
    This class handles basic repository access for the shop (CRUD operations)
    Entities handled by ShopService:
        - customers
        - locations
        - product categories
        - products
        - suppliers
 */
@Slf4j
@Service
public class ShopService {

    //Repositories
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    @Autowired
    public ShopService(CustomerRepository customerRepository,
                       ProductRepository productRepository,
                       OrderRepository orderRepository){
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    /*
        Repository access
     */
    public List<Customer> getAllCustomers(){return customerRepository.findAll();}

    public List<Product> getAllProducts(){return productRepository.findAll();}

/*•	You get a single java object as input. This object will contain the order timestamp, the delivery address and a list of products (product ID and quantity) contained in the order.
        •	You return an Order entity if the operation was successful. If not, you throw an exception.
        •	The service has to select a strategy for finding from which locations should the products be taken. See the strategy design pattern. The strategy should be selected based on a spring boot configuration. The following initial strategy should be created:
        o	Single location: find a single location that has all the required products (with the required quantities) in stock. If there are more such locations, simply take the first one based on the ID.
        •	The service then runs the strategy, obtaining a list of objects with the following structure: location, product, quantity (= how many items of the given product are taken from the given location). If the strategy is unable to find a suitable set of locations, it should throw an exception.
        •	The stocks need to be updated by subtracting the shipped goods.
        •	Afterwards the order is persisted in the database and returned.
*/

    @Transactional
    public Order createNewOrder(OrderSpecifications orderSpecifications){

        log.info("Creating order for customer: {}", orderSpecifications.getCustomer().getUser().getUsername());

        Order newOrder = new Order();

        //Set customer and shipping address
        newOrder.setCustomer(orderSpecifications.getCustomer());
        newOrder.setShippingAddress(orderSpecifications.getAddress());

        //Save order -> this step is needed to get a compliant ID for the order
        orderRepository.save(newOrder);

        //Set order details
        addDetailsToOrder(orderSpecifications.getShoppingCart(), newOrder);

        //Update status
        newOrder.setStatus(Order.Status.PROCESSING);

        //Persist with order details added
        orderRepository.save(newOrder);

        log.info("Succesfully created Order (#ID){}:", newOrder.getOrderId());
        log.info(newOrder.toString());

        return newOrder;
    }

    public OrderSpecifications createOrderSpecifications(OrderCreationDto request, String username){

        if(!DistanceCalculator.checkIfAddressIsValid(request.getAddress())){
            throw new InvalidShippmentAddressException();
        }
        if(request.getShoppingCart().isEmpty()){
            throw new EmptyShoppingCartException();
        }

        return OrderSpecifications.builder().customer(customerRepository.findByUser_Username(username)).request(request).build();
    }

    private void addDetailsToOrder(List<ShoppingCartEntry> request, Order order) {
        List<OrderDetail> details = request.stream().map(entry -> OrderDetail.builder()
                .order(order)
                .product(productRepository.findOne(entry.getProductId()))
                .quantity(entry.getQuantity())
                .orderDetailKey(new OrderDetailKey(order.getOrderId(), entry.getProductId()))
                .build()).collect(Collectors.toList());
        order.setOrderDetails(details);
    }
}
