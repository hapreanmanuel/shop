package ro.msg.learning.shop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.domain.*;
import ro.msg.learning.shop.dto.OrderCreationDto;
import ro.msg.learning.shop.dto.OrderSpecifications;
import ro.msg.learning.shop.dto.ResolvedOrderDetail;
import ro.msg.learning.shop.exception.EmptyShoppingCartException;
import ro.msg.learning.shop.exception.InvalidShippmentAddressException;
import ro.msg.learning.shop.exception.NoSuitableStrategyException;
import ro.msg.learning.shop.repository.*;

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
@Service
public class ShopService {

    //Repositories
    private final CustomerRepository customerRepository;
    private final ProductCategoryRepository productCategoryRepository ;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final LocationRepository locationRepository;

    @Autowired
    public ShopService(CustomerRepository customerRepository,
                       ProductCategoryRepository productCategoryRepository,
                       ProductRepository productRepository,
                       OrderRepository orderRepository,
                       OrderDetailRepository orderDetailRepository,
                       LocationRepository locationRepository){
        this.customerRepository = customerRepository;
        this.productCategoryRepository = productCategoryRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.locationRepository = locationRepository;

    }

    /*
        Repository access
     */
    //Customers
    public Customer getCustomer(int customerId){return customerRepository.findOne(customerId);}
   public List<Customer> getAllCustomers(){return customerRepository.findAll();}

    //Product
    public List<Product> getAllProducts(){return productRepository.findAll();}

    /*
        Orders
     */
    public Order getOrder(int orderId){return orderRepository.findOne(orderId);}
    public List<Order> getAllOrders(){return orderRepository.findAll();}
    public List<Order> getAllOrdesForCustomer(int customerId){
        return orderRepository.findByCustomer_CustomerId(customerId);
    }

    //Orderdetails
   public List<OrderDetail> getAllDetailsForOrder(int orderId){return orderDetailRepository.findByOrderDetailKey_OrderId(orderId); }

/*•	You get a single java object as input. This object will contain the order timestamp, the delivery address and a list of products (product ID and quantity) contained in the order.
        •	You return an Order entity if the operation was successful. If not, you throw an exception.
        •	The service has to select a strategy for finding from which locations should the products be taken. See the strategy design pattern. The strategy should be selected based on a spring boot configuration. The following initial strategy should be created:
        o	Single location: find a single location that has all the required products (with the required quantities) in stock. If there are more such locations, simply take the first one based on the ID.
        •	The service then runs the strategy, obtaining a list of objects with the following structure: location, product, quantity (= how many items of the given product are taken from the given location). If the strategy is unable to find a suitable set of locations, it should throw an exception.
        •	The stocks need to be updated by subtracting the shipped goods.
        •	Afterwards the order is persisted in the database and returned.
*/

    public OrderSpecifications createOrderSpecifications(OrderCreationDto request){

        if(!request.getAddress().checkIfValid()){
            throw new InvalidShippmentAddressException();
        }
        if(request.getShoppingCart().isEmpty()){
            throw new EmptyShoppingCartException();
        }

        return OrderSpecifications.builder().request(request).build();
    }

    //This method should be called after the order specifications have been processed -> resolution is not empty
    @Transactional
    public Order createNewOrder(OrderSpecifications orderSpecifications){

        if(orderSpecifications.getResolution().isEmpty()) {
            throw new NoSuitableStrategyException();
        }

        Order newOrder = new Order();

        //Set customer and shipping address
        newOrder.setCustomer(getCustomer(orderSpecifications.getCustomerId()));
        newOrder.setShippingAddress(orderSpecifications.getAddress());

        //Save order -> this step is needed to get a compliant ID for the order
        orderRepository.save(newOrder);

        //Set order details
        addDetailsToOrder(orderSpecifications.getResolution(), newOrder);

        //Persist with order details added
        orderRepository.save(newOrder);

        return newOrder;
    }

    @Transactional
    private void addDetailsToOrder(List<ResolvedOrderDetail> resolvedOrderDetails, Order order){
        List<OrderDetail> details = resolvedOrderDetails.stream().map(resolvedOrderDetail-> OrderDetail.builder()
                .order(order)
                .product(productRepository.findOne(resolvedOrderDetail.getProductId()))
                .quantity(resolvedOrderDetail.getQuantity())
                .orderDetailKey(new OrderDetailKey(order.getOrderId(),resolvedOrderDetail.getProductId()))
                .build()).collect(Collectors.toList());
        order.setOrderDetails(details);

        order.setLocation(locationRepository.findOne(resolvedOrderDetails.iterator().next().getLocationId()));
    }


}
