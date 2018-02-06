package ro.msg.learning.shop;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ro.msg.learning.shop.domain.misc.OrderSpecifications;
import ro.msg.learning.shop.domain.misc.ResolvedOrderDetail;
import ro.msg.learning.shop.domain.misc.ShoppingCartEntry;
import ro.msg.learning.shop.service.OrderService;
import ro.msg.learning.shop.service.StockService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StrategySelectionTests {

    @Autowired
    private OrderService orderService;

    @Autowired
    private StockService stockService;

    @Value("${shop.stock.strategy}")
    private String strategy;

    @Test
    public void printStrategyFromAppProperties() {
        System.out.println(strategy);
    }


    //This test is performed on the mock data initially added to the database

    // location1 - name = "mainLocation"
    //             stocks = 100 for every product
    // location 2 - name = "secondLocation"
    //             stocks = 200 for every product

    @Test
    public void singleStrategyTestSuite(){

        //Specification for an order for the first customer
        //This order should be possible from the first location
        OrderSpecifications orderSpecifications1 = new OrderSpecifications(1);
        orderSpecifications1.addShoppingCartEntry(new ShoppingCartEntry(1,10));
        orderSpecifications1.addShoppingCartEntry(new ShoppingCartEntry(2,25));

        //Expected to find a suitable strategy in the first location
        List<ResolvedOrderDetail> slStrategyOrder1 = stockService.getStrategy(orderSpecifications1);

        assertThat(slStrategyOrder1).isNotEmpty();
        assertThat(slStrategyOrder1.iterator().next().getLocationId()).isEqualTo(1);

        //Expected to find a suitable strategy in the second location
        orderSpecifications1.addShoppingCartEntry(new ShoppingCartEntry(3,102));
        List<ResolvedOrderDetail> slStrategyOrder2 = stockService.getStrategy(orderSpecifications1);

        assertThat(slStrategyOrder2).isNotEmpty();
        assertThat(slStrategyOrder2.iterator().next().getLocationId()).isEqualTo(2);

        //Should not find a suitable strategy
        orderSpecifications1.addShoppingCartEntry(new ShoppingCartEntry(4,202));
        List<ResolvedOrderDetail> slStrategyOrder3 = stockService.getStrategy(orderSpecifications1);

        assertThat(slStrategyOrder3).isEmpty();

    }

}
