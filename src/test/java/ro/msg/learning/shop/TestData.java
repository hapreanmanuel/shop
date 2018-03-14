package ro.msg.learning.shop;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ro.msg.learning.shop.domain.*;
import ro.msg.learning.shop.repository.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TestData implements CommandLineRunner{

    private final CustomerRepository customerRepository;
    private final LocationRepository locationRepository;
    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final AuthorityRepository authorityRepository;
    private final SupplierRepository supplierRepository;
    private final StockRepository stockRepository;

    @Autowired
    public TestData(CustomerRepository customerRepository, LocationRepository locationRepository, ProductRepository productRepository, ProductCategoryRepository productCategoryRepository, AuthorityRepository authorityRepository, SupplierRepository supplierRepository, StockRepository stockRepository) {
        this.customerRepository = customerRepository;
        this.locationRepository = locationRepository;
        this.productRepository = productRepository;
        this.productCategoryRepository = productCategoryRepository;
        this.authorityRepository = authorityRepository;
        this.supplierRepository=supplierRepository;
        this.stockRepository=stockRepository;
    }

    @Override
    public void run(String... args) {
        log.info("====================================================");
        log.info("Creating mock data for testing scope");

        List<ProductCategory> categories = Arrays.asList(
                ProductCategory.builder().name("Gadgets").description("We sell top quality electronic accessories!").build(),
                ProductCategory.builder().name("Desktop Computers").description("The best components for desktop computers.").build(),
                ProductCategory.builder().name("Full HD flat screens for the best user experience").description("Flat Screens").build()
        );
        productCategoryRepository.save(categories);

        List<Supplier> suppliers = Arrays.asList(
                Supplier.builder().name("ALTEX").build(),
                Supplier.builder().name("SONY").build()
        );
        supplierRepository.save(suppliers);

        List<Product> products = Arrays.asList(
                Product.builder().name("FLR214-222").price(BigDecimal.valueOf(10)).weight(0.3).category(categories.get(0)).supplier(suppliers.get(0)).description("Ultra professional flashlight").build(),
                Product.builder().name("FLR217-280").price(BigDecimal.valueOf(20)).weight(0.4).category(categories.get(0)).supplier(suppliers.get(0)).description("Ultra professional flashlight with extended battery").build(),
                Product.builder().name("AMD I5").price(BigDecimal.valueOf(569)).weight(12.3).category(categories.get(1)).supplier(suppliers.get(0)).description("Accessible desktop computer").build(),
                Product.builder().name("GTX-1060").price(BigDecimal.valueOf(134)).weight(2.7).category(categories.get(1)).supplier(suppliers.get(0)).description("Latest generation GPU").build(),
                Product.builder().name("LG-1231").price(BigDecimal.valueOf(10)).weight(0.3).category(categories.get(2)).supplier(suppliers.get(1)).description("Ultra wide smart screen").build()
        );
        productRepository.save(products);

        List<Location> locations = Arrays.asList(
                Location.builder().name("Main Location").address(Address.builder()
                        .fullAddress("Str. Dorobantilor 77").city("Cluj-Napoca").country("Romania").region("CJ").build()).build(),
                Location.builder().name("Secondary Location").address(Address.builder()
                        .fullAddress("Str. Traian 23B7").city("Bucuresti").country("Romania").region("B").build()).build()
        );
        locationRepository.save(locations);

        List<Authority> authorities = Arrays.asList(
                Authority.builder().authorityType(AuthorityType.ADMIN).build(),
                Authority.builder().authorityType(AuthorityType.CUSTOMER).build()
        );
        authorityRepository.save(authorities);

        List<Customer> customers = Arrays.asList(
                Customer.builder().user(User.builder().authority(authorities.get(0))
                        .username("sa").password("").build())
                        .firstName("").lastName("").build(),
                Customer.builder().user(User.builder().authority(authorities.get(1))
                        .username("test").password("test").build())
                        .firstName("test").lastName("test").build()
        );
        customerRepository.save(customers);

        log.info("Creating stocks for locations ");

        final int stockSizeMain = 100;
        final int stockSizeSecondary = 200;

        log.info("Creating stocks for location {} : {} pieces of each product.", locations.get(0), stockSizeMain);
        createStocks(locations.get(0),products , stockSizeMain);

        log.info("Creating stocks for location {} : {} pieces of each product.", locations.get(1), stockSizeSecondary);
        createStocks(locations.get(1), products,stockSizeSecondary );

        log.info("====================================================");
    }

    private void createStocks(Location location, List<Product> products, int size){
        stockRepository.save(products.stream().map(product -> Stock.builder()
                .location(location)
                .product(product)
                .quantity(size)
                .stockKey(StockKey.builder().locationId(location.getLocationId()).productId(product.getProductId())
                        .build()).build()).collect(Collectors.toList()));
    }

}

