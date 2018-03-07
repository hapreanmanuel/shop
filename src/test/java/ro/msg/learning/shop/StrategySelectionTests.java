//package ro.msg.learning.shop;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.core.env.Environment;
//import org.springframework.test.context.junit4.SpringRunner;
//import ro.msg.learning.shop.domain.Address;
//import ro.msg.learning.shop.domain.Customer;
//import ro.msg.learning.shop.dto.OrderCreationDto;
//import ro.msg.learning.shop.dto.OrderSpecifications;
//import ro.msg.learning.shop.dto.ShoppingCartEntry;
//import ro.msg.learning.shop.service.ShopService;
//import ro.msg.learning.shop.service.StockService;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest(properties="spring.jpa.hibernate.ddl-auto=none")
//public class StrategySelectionTests {
//
//    @Autowired
//    private StockService stockService;
//
//    @Autowired
//    private ShopService shopService;
//
//    @Autowired
//    private Environment environment;
//
//    @Test
//    public void printStrategyFromAppProperties() {
//
//        //From application.properties file
//        System.out.println(environment.getProperty("shop.strategy"));
//    }
//
//
//    //This test is performed on the mock data initially added to the database
//
//    // location1 - name = "mainLocation"
//    //             stocks = 100 for every product
//    // location 2 - name = "secondLocation"
//    //             stocks = 200 for every product
//
//    @Test
//    public void singleStrategyTestSuite(){
//
//        Customer customer = shopService.getCustomer(1);
//
//        OrderCreationDto o = new OrderCreationDto();
//
//        OrderSpecifications os;
//
//        o.setAddress(Address.builder()
//                .region("CJ")
//                .country("Romania")
//                .city("Cluj-Napoca")
//                .fullAddress("Home street avenue 111").build());
//
//        o.getShoppingCart().add(new ShoppingCartEntry(1,10));
//        o.getShoppingCart().add(new ShoppingCartEntry(2,25));
//
//        //Specification for an order for the first customer
//        //This order should be possible from the first location
//        os = shopService.createOrderSpecifications(o, customer.getUser().getUsername());
//
//        //Expected to find a suitable strategy in the first location
//        stockService.processRequest(os);
//
//        assertThat(os.getResolution()).isNotEmpty();
//        assertThat(os.getResolution().iterator().next().getLocationId()).isEqualTo(1);
//
//        //Expected to find a suitable strategy in the second location
//        o.getShoppingCart().add(new ShoppingCartEntry(3,102));
//
//        os = shopService.createOrderSpecifications(o, customer.getUser().getUsername());
//
//        stockService.processRequest(os);
//
//        assertThat(os.getResolution()).isNotEmpty();
//        assertThat(os.getResolution().iterator().next().getLocationId()).isEqualTo(2);
//
//        //Should not find a suitable strategy
//        o.getShoppingCart().add(new ShoppingCartEntry(4,202));
//        os = shopService.createOrderSpecifications(o, customer.getUser().getUsername());
//
//        stockService.processRequest(os);
//
//        assertThat(os.getResolution()).isEmpty();
//
//    }
//
//    @Test
//    public void closestStrategyTest(){
//
//        Customer customer = shopService.getCustomer(1);
//
//        OrderCreationDto o = new OrderCreationDto();
//        OrderSpecifications os;
//
//        o.setAddress(Address.builder()
//                .region("CT")
//                .country("Romania")
//                .city("Constanta")
//                .fullAddress("Calea laptelui 22").build());
//
//        o.getShoppingCart().add(new ShoppingCartEntry(1,10));
//        o.getShoppingCart().add(new ShoppingCartEntry(2,25));
//
//        /*
//            2nd location is closoer to the shippment address
//         */
//        os = shopService.createOrderSpecifications(o, customer.getUser().getUsername());
//
//        //Expected to find a suitable strategy in the first location
//        stockService.processRequest(os);
//
//        assertThat(os.getResolution()).isNotEmpty();
//        assertThat(os.getResolution().iterator().next().getLocationId()).isEqualTo(2);
//
//    }
//}
