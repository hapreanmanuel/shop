package ro.msg.learning.shop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.domain.*;
import ro.msg.learning.shop.dto.OrderSpecifications;
import ro.msg.learning.shop.dto.ResolvedOrderDetail;
import ro.msg.learning.shop.exception.EmptyShoppingCartException;
import ro.msg.learning.shop.exception.InvalidLocationException;
import ro.msg.learning.shop.exception.NoSuitableStrategyException;
import ro.msg.learning.shop.repository.*;
import ro.msg.learning.shop.utility.BeansManager;

import java.util.ArrayList;
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
public class ShopService implements Injectable{

    //Repositories
    private final CustomerRepository customerRepository;
    private final ProductCategoryRepository productCategoryRepository ;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;

    //TODO - remove stock service
    private StockService stockService;

    @Autowired
    public ShopService(CustomerRepository customerRepository,
                       ProductCategoryRepository productCategoryRepository,
                       ProductRepository productRepository,
                       OrderRepository orderRepository,
                       OrderDetailRepository orderDetailRepository){
        this.customerRepository = customerRepository;
        this.productCategoryRepository = productCategoryRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;

    }

    @Override
    public void inject(BeansManager beansManager) {
        this.stockService = beansManager.getStockService();
    }

    /*
        Repository access
     */
    //Customers
    public Customer getCustomer(int customerId){return customerRepository.findOne(customerId);}
    public Customer getCustomerByUserame(String customerUsername){return customerRepository.findByUserName(customerUsername); }
    public List<Customer> getAllCustomers() { return customerRepository.findAll(); }
    public void addCustomer(Customer customer) { customerRepository.save(customer);}
    public void deleteCustomer(int customerId) {customerRepository.delete(customerId);}

    //Product category
    public ProductCategory getProductCategory(int categoryId){return productCategoryRepository.findOne(categoryId);}
    public List<ProductCategory> getAllProductCategories() {return productCategoryRepository.findAll();}
    public void addProductCategory(ProductCategory productCategory){productCategoryRepository.save(productCategory);}
    public void deleteProductCategory(ProductCategory productCategory){productCategoryRepository.delete(productCategory);}

    //Product
    public Product getProduct(int productId){return productRepository.findOne(productId);}
    public List<Product> getAllProducts(){return productRepository.findAll();}
    public void addProduct(Product product){productRepository.save(product);}
    public void deleteProduct(Product product){productRepository.delete(product);}

    /*
        Orders
     */

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
        basicOrderSpecifications.setAddress(Address.builder().build());
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
        newOrder.setCustomer(getCustomer(orderSpecifications.getCustomerId()));
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
            orderDetail.setProduct(getProduct(resolvedOrderDetail.getProductId()));
            orderDetail.setOrder(order);
            orderDetails.add(orderDetail);
        });

        addOrderDetails(orderDetails);

        order.setOrderDetails(orderDetails);

        //Set shipment location
        order.setLocation(stockService.getLocation(resolvedOrderDetails.iterator().next().getLocationId()));

        //Update order (order details added after the actual order to the db)
        updateOrder(order);
    }


}
