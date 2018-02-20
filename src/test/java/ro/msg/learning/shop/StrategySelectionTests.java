package ro.msg.learning.shop;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringRunner;
import ro.msg.learning.shop.domain.Address;
import ro.msg.learning.shop.dto.OrderSpecifications;
import ro.msg.learning.shop.dto.ResolvedOrderDetail;
import ro.msg.learning.shop.dto.ShoppingCartEntry;
import ro.msg.learning.shop.service.ShopService;
import ro.msg.learning.shop.service.StockService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StrategySelectionTests {

    @Autowired
    private StockService stockService;

    @Autowired
    private ShopService shopService;

    @Autowired
    private Environment environment;

    @Test
    public void printStrategyFromAppProperties() {

        //From application.properties file
        System.out.println(environment.getProperty("shop.strategy"));
    }


    //This test is performed on the mock data initially added to the database

    // location1 - name = "mainLocation"
    //             stocks = 100 for every product
    // location 2 - name = "secondLocation"
    //             stocks = 200 for every product

    @Test
    public void singleStrategyTestSuite(){

        //Shipment address
        Address address = Address.builder().build();
        address.setCity("Cluj-Napoca");
        address.setCountry("Romania");
        address.setRegion("CJ");
        address.setFullAddress("Dorobantilor 112B AP6");

        //Specification for an order for the first customer
        //This order should be possible from the first location
        OrderSpecifications orderSpecifications1 = shopService.createBasicOrderSpecificationsForCustomer(1);
        orderSpecifications1.setAddress(address);
        orderSpecifications1.getShoppingCart().add(new ShoppingCartEntry(1,10));
        orderSpecifications1.getShoppingCart().add(new ShoppingCartEntry(2,25));

        //Expected to find a suitable strategy in the first location
        stockService.processRequest(orderSpecifications1);

        assertThat(orderSpecifications1.getResolution()).isNotEmpty();
        assertThat(orderSpecifications1.getResolution().iterator().next().getLocationId()).isEqualTo(1);

        //Expected to find a suitable strategy in the second location
        orderSpecifications1.getShoppingCart().add(new ShoppingCartEntry(3,102));

        stockService.processRequest(orderSpecifications1);

        assertThat(orderSpecifications1.getResolution()).isNotEmpty();
        assertThat(orderSpecifications1.getResolution().iterator().next().getLocationId()).isEqualTo(2);

        //Should not find a suitable strategy
        orderSpecifications1.getShoppingCart().add(new ShoppingCartEntry(4,202));
        stockService.processRequest(orderSpecifications1);

        assertThat(orderSpecifications1.getResolution()).isEmpty();

    }


}
