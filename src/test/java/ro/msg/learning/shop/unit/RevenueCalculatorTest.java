package ro.msg.learning.shop.unit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ro.msg.learning.shop.configuration.RevenueConfig;
import ro.msg.learning.shop.domain.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RevenueCalculatorTest {

    private Order o1 = new Order();
    private Order o2 = new Order();
    private Location l = Location.builder().address(Address.builder()
            .fullAddress("Home")
            .city("Alba")
            .country("Romania")
            .region("AB").build()).build();
    private Product p1 = Product.builder().price(new BigDecimal(6)).build();
    private Product p2 = Product.builder().price(new BigDecimal(10)).build();

    @Test
    public void checkGetRevenueForLocation(){

        //Populate orders
        o1.setLocation(l);

        List<OrderDetail> o1d = Arrays.asList(OrderDetail.builder().order(o1).product(p1).quantity(3).build());

        o1.setOrderDetails(o1d);


        o2.setLocation(l);

        List<OrderDetail> o2d = Arrays.asList(OrderDetail.builder().order(o2).product(p1).quantity(1).build(),
                OrderDetail.builder().order(o2).product(p2).quantity(3).build());

        o2.setOrderDetails(o2d);


        BigDecimal total = new BigDecimal(6*3 + 6 + 3*10);

        List<Order> orders = Arrays.asList(o1, o2);

        Revenue rev = RevenueConfig.getRevenueForLocation(l, orders);

        assertThat(rev.getLocation()).isEqualTo(l);

        assertThat(rev.getTotal()).isEqualTo(total);

        assertThat(o2.isRevenued()).isEqualTo(true);

    }
}
