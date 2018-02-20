package ro.msg.learning.shop.mockdata;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ro.msg.learning.shop.domain.*;
import ro.msg.learning.shop.dto.OrderSpecifications;
import ro.msg.learning.shop.dto.ShoppingCartEntry;
import ro.msg.learning.shop.repository.*;
import ro.msg.learning.shop.service.ShopService;
import ro.msg.learning.shop.service.StockService;


import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/*
	Add some entries to the database

	- this component adds 4 customers, 2 locations, 3 product categories, 5 suppliers and 5 products
	- uses JpaRepositories directly

*/
@Component
public class AddMockDataToDatabase implements CommandLineRunner{

    //Work directly on repositories
    private CustomerRepository customerRepository;
    private LocationRepository locationRepository;
    private ProductCategoryRepository productCategoryRepository;
    private ProductRepository productRepository;
    private SupplierRepository supplierRepository;
    private AuthorityRepository authorityRepository;

    private final StockService stockService;
    private final ShopService shopService;
    private final PasswordEncoder encoder;

    private final List<Integer> stockSizeForLocations = Arrays.asList(100,200);

    @Autowired
    public AddMockDataToDatabase(CustomerRepository customerRepository, LocationRepository locationRepository, ProductCategoryRepository productCategoryRepository, ProductRepository productRepository, SupplierRepository supplierRepository, ShopService shopService, StockService stockService, AuthorityRepository authorityRepository, PasswordEncoder encoder) {
        this.customerRepository = customerRepository;
        this.locationRepository = locationRepository;
        this.productCategoryRepository = productCategoryRepository;
        this.productRepository = productRepository;
        this.supplierRepository = supplierRepository;
        this.shopService=shopService;
        this.stockService=stockService;
        this.authorityRepository = authorityRepository;
        this.encoder = encoder;
    }

    @Override
    public void run(String... strings) {
        initAuthorities();
        initCustomers();
        initShop();
        initStocks();
        initOrder();
    }

    private void initAuthorities(){
        Arrays.stream(AuthorityType.values()).forEach(authorityType ->  authorityRepository.save(Authority.builder().authorityType(authorityType).build()));
    }

    private void initCustomers(){

        Authority customerType = authorityRepository.findByAuthorityType(AuthorityType.CUSTOMER);

        List<Customer> customers = Arrays.asList(
                Customer.builder()
                        .user(User.builder()
                                .username("djones223")
                                .password(encoder.encode("mrwhite"))
                                .authority(customerType)
                                .build())
                        .firstName("Davidson")
                        .lastName("Jones")
                        .build(),
                Customer.builder()
                        .user(User.builder()
                                .username("eassociation")
                                .password(encoder.encode("power"))
                                .authority(customerType)
                                .build())
                        .firstName("Electronica")
                        .lastName("Association")
                        .build(),
                Customer.builder()
                        .user(User.builder()
                                .username("test")
                                .password(encoder.encode("test"))
                                .authority(customerType)
                                .build())
                        .firstName("Tester")
                        .lastName("")
                        .build(),
                Customer.builder()
                        .user(User.builder()
                                .username("dummy")
                                .password(encoder.encode("dummy"))
                                .authority(customerType)
                                .build())
                        .firstName("Dummy")
                        .lastName("")
                        .build());

        customerRepository.save(customers);

    }

    private void initShop(){
        //Locations
        List<Location> locations = Arrays.asList(
                Location.builder()
                        .name("Main Location")
                        .address(Address.builder()
                                .fullAddress("Str. Dorobantilor 77")
                                .city("Cluj-Napoca")
                                .country("Romania")
                                .region("CJ")
                                .build())
                        .build(),
                Location.builder()
                        .name("Secondary Location")
                        .address(Address.builder()
                                .fullAddress("Str. Traian 23B7")
                                .city("Bucuresti")
                                .country("Romania")
                                .region("B")
                                .build())
                        .build());

        locationRepository.save(locations);

        //Product categories
        List<ProductCategory> productCategories = Arrays.asList(
                ProductCategory.builder()
                        .name("Gadgets")
                        .description("We sell top quality electronic accessories!")
                        .build(),
                ProductCategory.builder()
                        .name("Desktop Computers")
                        .description("The best components for desktop computers.")
                        .build(),
                ProductCategory.builder()
                        .name("Flat Screens")
                        .description("Full HD flat screens for the best user experience")
                        .build());

        productCategoryRepository.save(productCategories);

        //Suppliers
        List<Supplier> suppliers = Arrays.asList(
                Supplier.builder()
                        .name("PC-Garage")
                        .build(),
                Supplier.builder()
                        .name("Altex")
                        .build(),
                Supplier.builder()
                        .name("NVidia")
                        .build(),
                Supplier.builder()
                        .name("Intel")
                        .build(),
                Supplier.builder()
                        .name("Jay Electronics")
                        .build());

        supplierRepository.save(suppliers);

        //Products
        List<Product> products = Arrays.asList(
                Product.builder()
                        .name("FLR214-222")
                        .description("Ultra professional flashlight")
                        .price(BigDecimal.valueOf(10))
                        .weight(0.3)
                        .category(productCategories.get(0))
                        .supplier(suppliers.get(4))
                        .build(),
                Product.builder()
                        .name("FLR217-280")
                        .description("Ultra professional flashlight with extended battery capacity")
                        .price(BigDecimal.valueOf(20))
                        .weight(0.4)
                        .category(productCategories.get(0))
                        .supplier(suppliers.get(4))
                        .build(),
                Product.builder()
                        .name("AMD I5")
                        .description("Accessible desktop computer")
                        .price(BigDecimal.valueOf(569))
                        .weight(12.3)
                        .category(productCategories.get(1))
                        .supplier(suppliers.get(0))
                        .build(),
                Product.builder()
                        .name("GTX-1060")
                        .description("Latest generation GPU")
                        .price(BigDecimal.valueOf(134))
                        .weight(2.4)
                        .category(productCategories.get(1))
                        .supplier(suppliers.get(2))
                        .build(),
                Product.builder()
                        .name("LG-1231")
                        .description("Ultra wide smart screen")
                        .price(BigDecimal.valueOf(334))
                        .weight(7.8)
                        .category(productCategories.get(2))
                        .supplier(suppliers.get(1))
                        .build());

        productRepository.save(products);
    }

    private void initStocks(){
        stockService.createStocksForLocation(stockService.getAllLocations().get(0),stockSizeForLocations.get(0));
        stockService.createStocksForLocation(stockService.getAllLocations().get(1),stockSizeForLocations.get(1));
    }

    private void initOrder(){
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

        /*
            Order creation flow
         */
        stockService.processRequest(orderSpecifications);

        //Submit the order by calling the order creation method from orderService
        Order mockOrder = shopService.createNewOrder(orderSpecifications);

        stockService.updateStockForOrder(mockOrder);
    }

}
