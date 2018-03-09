package ro.msg.learning.shop.order;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ro.msg.learning.shop.domain.Address;
import ro.msg.learning.shop.domain.Order;
import ro.msg.learning.shop.dto.OrderCreationDto;
import ro.msg.learning.shop.dto.OrderSpecifications;
import ro.msg.learning.shop.dto.ShoppingCartEntry;
import ro.msg.learning.shop.service.ShopService;
import ro.msg.learning.shop.service.StockService;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderBehaviorTest {

    private final String username = "test";

    private final List<ShoppingCartEntry> wishList = Arrays.asList(
            new ShoppingCartEntry(1,10),
            new ShoppingCartEntry(2,20)
    );
    private final Address address = Address.builder()
            .city("Cluj-Napoca").country("Romania").region("CJ").fullAddress("Str. Dave Mira 111").build();


    @Autowired
    private ShopService shopService;

    @Autowired
    private StockService stockService;

    @Test
    public void orderOperations(){
        log.info("=====================================================================");
        log.info("Running test: Order Creation and Processing");


        OrderCreationDto request = new OrderCreationDto();
        request.setShoppingCart(wishList);
        request.setAddress(address);

        OrderSpecifications os = shopService.createOrderSpecifications(request,username);

        log.info("Request: {}", os.toString());

        Order order = shopService.createNewOrder(os);

        log.info("Created Order: {}", order.toString());

        assertThat(order).isInstanceOf(Order.class);
        assertThat(order.isRevenued()).isEqualTo(false);
        assertThat(order.getLocation()).isEqualTo(null);
        assertThat(order.getStatus()).isEqualByComparingTo(Order.Status.PROCESSING);

        order = stockService.processOrder(order.getOrderId());

        log.info("Processed Order: {}", order.toString());

        assertThat(order.getLocation()).isNotEqualTo(null);
        assertThat(order.getStatus()).isEqualByComparingTo(Order.Status.COMPLETE);

    }
}
