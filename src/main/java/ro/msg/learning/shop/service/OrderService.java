package ro.msg.learning.shop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.domain.misc.OrderDetailKey;
import ro.msg.learning.shop.domain.misc.ResolvedOrderDetail;
import ro.msg.learning.shop.domain.tables.Order;
import ro.msg.learning.shop.domain.misc.OrderSpecifications;
import ro.msg.learning.shop.domain.tables.OrderDetail;
import ro.msg.learning.shop.repository.OrderDetailRepository;
import ro.msg.learning.shop.repository.OrderRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private ShopService shopService;
    @Autowired
    private StockService stockService;

    //Constructor
    public OrderService(OrderRepository orderRepository,OrderDetailRepository orderDetailRepository, ShopService shopService, StockService stockService){this.orderRepository = orderRepository; this.orderDetailRepository=orderDetailRepository ;this.shopService = shopService; this.stockService=stockService;}

    //Orders
    public Order getOrder(int orderId){return orderRepository.findOne(orderId);}

    public List<Order> getAllOrders(){return orderRepository.findAll();}

    public List<Order> getAllOrdesForCustomer(int customerId){return getAllOrders().stream().filter(order -> (order.getCustomer().getCustomerId()==customerId)).collect(Collectors.toList());}

    //Orderdetails
    public void addOrderDetail(OrderDetail orderDetail){ orderDetailRepository.save(orderDetail);}

    public List<OrderDetail> getAllOrderDetails(){return orderDetailRepository.findAll();}

    public List<OrderDetail> getAllDetailsForOrder(int orderId){return getAllOrderDetails().stream().filter(orderDetail -> orderDetail.getOrderDetailKey().getOrderId()==orderId).collect(Collectors.toList()); }

    private void addOrder(Order order){orderRepository.save(order);}

    private void updateOrder(Order order){orderRepository.save(order);}

/*•	You get a single java object as input. This object will contain the order timestamp, the delivery address and a list of products (product ID and quantity) contained in the order.
        •	You return an Order entity if the operation was successful. If not, you throw an exception.
        •	The service has to select a strategy for finding from which locations should the products be taken. See the strategy design pattern. The strategy should be selected based on a spring boot configuration. The following initial strategy should be created:
        o	Single location: find a single location that has all the required products (with the required quantities) in stock. If there are more such locations, simply take the first one based on the ID.
        •	The service then runs the strategy, obtaining a list of objects with the following structure: location, product, quantity (= how many items of the given product are taken from the given location). If the strategy is unable to find a suitable set of locations, it should throw an exception.
        •	The stocks need to be updated by subtracting the shipped goods.
        •	Afterwards the order is persisted in the database and returned.

*/
    //TODO exception handling
    public Order createNewOrder(OrderSpecifications orderSpecifications) throws RuntimeException {
        //Create a new order
        Order newOrder = new Order();

        //Set customer and shipping address
        newOrder.setCustomer(shopService.getCustomer(orderSpecifications.getCustomerId()));
        newOrder.setShippingAddress(orderSpecifications.getAddress());

        //Get strategy
        List<ResolvedOrderDetail> resolvedOrderDetails= stockService.getStrategy(orderSpecifications);

        if(resolvedOrderDetails.isEmpty()){
            //Throw exception
        }else{
            //Save the order to db
            addOrder(newOrder);
        }

        //Create the order details based on the shipment strategy
        List<OrderDetail> orderDetails = new ArrayList<>();
        resolvedOrderDetails.forEach(resolvedOrderDetail ->{
                OrderDetail orderDetail = new OrderDetail(new OrderDetailKey(newOrder.getOrderId(), resolvedOrderDetail.getProductId()));
                orderDetail.setQuantity(resolvedOrderDetail.getQuantity());
                orderDetail.setProduct(shopService.getProduct(resolvedOrderDetail.getProductId()));
                orderDetail.setOrder(newOrder);
                orderDetails.add(orderDetail);
                addOrderDetail(orderDetail);    //Save to database happens here -> todo: rework this
                });

        newOrder.setOrderDetails(orderDetails);

        //Set delivery location
        newOrder.setLocation(shopService.getLocation(resolvedOrderDetails.iterator().next().getLocationId()));

        /*
            Save the order to the database and export stocks

            (?) maybe this should be in a different class

         */

        //Add order to DB
        updateOrder(newOrder);

        //UpdateStocks
        stockService.updateStockForResolvedOrderDetails(resolvedOrderDetails);

        return newOrder;
    }
}






