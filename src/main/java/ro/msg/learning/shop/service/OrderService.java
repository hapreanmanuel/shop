package ro.msg.learning.shop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.domain.misc.Address;
import ro.msg.learning.shop.domain.misc.OrderDetailKey;
import ro.msg.learning.shop.domain.misc.ResolvedOrderDetail;
import ro.msg.learning.shop.domain.tables.Order;
import ro.msg.learning.shop.domain.misc.OrderSpecifications;
import ro.msg.learning.shop.domain.tables.OrderDetail;
import ro.msg.learning.shop.exceptions.EmptyShoppingCartException;
import ro.msg.learning.shop.exceptions.InvalidLocationException;
import ro.msg.learning.shop.exceptions.NoSuitableStrategyException;
import ro.msg.learning.shop.repository.OrderDetailRepository;
import ro.msg.learning.shop.repository.OrderRepository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    //Repository
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;

    //Services
    private final ShopService shopService;

    //TODO - remove stock service
    private final StockService stockService;

    @Autowired
    public OrderService(OrderRepository orderRepository,OrderDetailRepository orderDetailRepository, ShopService shopService, StockService stockService){
        this.orderRepository = orderRepository;
        this.orderDetailRepository=orderDetailRepository;
        this.shopService = shopService;
        this.stockService=stockService;
    }

    //Orders
    public Order getOrder(int orderId){return orderRepository.findOne(orderId);}
    public List<Order> getAllOrders(){return orderRepository.findAll();}
    public List<Order> getAllOrdesForCustomer(int customerId){
        return getAllOrders().stream().filter(order -> (order.getCustomer().getCustomerId()==customerId)).collect(Collectors.toList());
    }
    private void addOrder(Order order){orderRepository.save(order);}
    private void updateOrder(Order order){orderRepository.save(order);}

    //Orderdetails
    private void addOrderDetail(OrderDetail orderDetail){ orderDetailRepository.save(orderDetail);}
    private void addOrderDetails(List<OrderDetail> orderDetails){orderDetails.forEach(this::addOrderDetail);}
    public List<OrderDetail> getAllDetailsForOrder(int orderId){return orderDetailRepository.findByOrderDetailKey_OrderId(orderId); }

/*•	You get a single java object as input. This object will contain the order timestamp, the delivery address and a list of products (product ID and quantity) contained in the order.
        •	You return an Order entity if the operation was successful. If not, you throw an exception.
        •	The service has to select a strategy for finding from which locations should the products be taken. See the strategy design pattern. The strategy should be selected based on a spring boot configuration. The following initial strategy should be created:
        o	Single location: find a single location that has all the required products (with the required quantities) in stock. If there are more such locations, simply take the first one based on the ID.
        •	The service then runs the strategy, obtaining a list of objects with the following structure: location, product, quantity (= how many items of the given product are taken from the given location). If the strategy is unable to find a suitable set of locations, it should throw an exception.
        •	The stocks need to be updated by subtracting the shipped goods.
        •	Afterwards the order is persisted in the database and returned.
*/

    public OrderSpecifications createBasicOrderSpecificationsForCustomer(int customerId){

        OrderSpecifications basicOrderSpecifications = new OrderSpecifications();

        basicOrderSpecifications.setCustomerId(customerId);
        basicOrderSpecifications.setOrderCreationTimestamp(new Timestamp(System.currentTimeMillis()));
        basicOrderSpecifications.setAddress(new Address());
        basicOrderSpecifications.setShoppingCart(new ArrayList<>());

        return basicOrderSpecifications;
    }

    public Order createNewOrder(OrderSpecifications orderSpecifications){

        /*
            Check order creation specifications validity
         */
        //TODO - invalid location examination- some method to check if an address is valid
        if(orderSpecifications.getAddress().getFullAddress().equals("")) throw new InvalidLocationException();
        if(orderSpecifications.getShoppingCart().isEmpty()) throw new EmptyShoppingCartException();

        /*
            Run selection strategy
         */
        List<ResolvedOrderDetail> resolvedOrderDetails= stockService.getStrategy(orderSpecifications);

        //Check that there is a solution for the order requests
        if(resolvedOrderDetails.isEmpty()) throw new NoSuitableStrategyException();

        /*
            Create a new order
         */
        Order newOrder = new Order();

        //Set customer and shipping address
        newOrder.setCustomer(shopService.getCustomer(orderSpecifications.getCustomerId()));
        newOrder.setShippingAddress(orderSpecifications.getAddress());

        //Save the order to db
        addOrder(newOrder);

        addOrderDetailsToOrder(resolvedOrderDetails, newOrder);

        /*
            Export stocks
            (?) maybe this should be in a different class
         */
        //TODO - move logic to stock service class where income statistics are being performed
        // -> maybe via an Aspect class
        //UpdateStocks
        stockService.updateStockForResolvedOrderDetails(resolvedOrderDetails);

        return newOrder;
    }

    private void addOrderDetailsToOrder(List<ResolvedOrderDetail> resolvedOrderDetails, Order order){

        List<OrderDetail> orderDetails = new ArrayList<>();
        resolvedOrderDetails.forEach(resolvedOrderDetail ->{
            OrderDetail orderDetail = new OrderDetail(new OrderDetailKey(order.getOrderId(), resolvedOrderDetail.getProductId()));
            orderDetail.setQuantity(resolvedOrderDetail.getQuantity());
            orderDetail.setProduct(shopService.getProduct(resolvedOrderDetail.getProductId()));
            orderDetail.setOrder(order);
            orderDetails.add(orderDetail);
        });

        addOrderDetails(orderDetails);

        order.setOrderDetails(orderDetails);

        //Set shipment location
        order.setLocation(shopService.getLocation(resolvedOrderDetails.iterator().next().getLocationId()));

        //Update order (order details added after the actual order to the db)
        updateOrder(order);
    }
}






