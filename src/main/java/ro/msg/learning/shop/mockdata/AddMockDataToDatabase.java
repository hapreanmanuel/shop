package ro.msg.learning.shop.mockdata;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import ro.msg.learning.shop.domain.*;
import ro.msg.learning.shop.repository.*;


import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/*
	Add some entries to the database

	- this component adds 4 customers, 2 locations, 3 product categories, 5 suppliers and 5 products
	- uses JpaRepositories directly

*/
@Component
public class AddMockDataToDatabase implements CommandLineRunner, Ordered {
    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE-2;
    }

    //Work directly on repositories
    private CustomerRepository customerRepository;
    private LocationRepository locationRepository;
    private ProductCategoryRepository productCategoryRepository;
    private ProductRepository productRepository;
    private SupplierRepository supplierRepository;

    @Autowired
    public AddMockDataToDatabase(CustomerRepository customerRepository, LocationRepository locationRepository, ProductCategoryRepository productCategoryRepository, ProductRepository productRepository, SupplierRepository supplierRepository) {
        this.customerRepository = customerRepository;
        this.locationRepository = locationRepository;
        this.productCategoryRepository = productCategoryRepository;
        this.productRepository = productRepository;
        this.supplierRepository = supplierRepository;
    }

    @Override
    public void run(String... strings) {

        //Customers
        List<Customer> mockCustomers = Arrays.asList(
                Customer.builder()
                        .firstName("Davidson")
                        .lastName("Jones")
                        .userName("djones223")
                        .build(),
                Customer.builder()
                        .firstName("Electronica")
                        .lastName("Association")
                        .userName("electrassociation")
                        .build(),
                Customer.builder()
                        .firstName("Tester")
                        .lastName("")
                        .userName("testuser")
                        .build(),
                Customer.builder()
                        .firstName("dummyFirstName")
                        .lastName("dummyLastName")
                        .userName("dummyuser")
                        .build());

        customerRepository.save(mockCustomers);

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
}
