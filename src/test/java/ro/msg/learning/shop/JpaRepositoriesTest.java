package ro.msg.learning.shop;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ro.msg.learning.shop.domain.*;
import ro.msg.learning.shop.repository.CustomerRepository;
import ro.msg.learning.shop.service.ShopService;
import ro.msg.learning.shop.service.StockService;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JpaRepositoriesTest {

    @Autowired
    private StockService stockService;
    @Autowired
    private ShopService shopService;

    @Autowired
    private CustomerRepository customerRepository;

    public JpaRepositoriesTest(){}

    @Test
    public void OrderDetailRepositoryTest(){
        //Test the find method by orderId and productId
    }


    @Test
    public void stockRepositoryTest(){

        List<Location>  allLocations = stockService.getAllLocations();
        List<Product>   allProducts = shopService.getAllProducts();

        List<Stock> stockLoc1 = stockService.getStocksForLocation(1);
        List<Stock> stockProd1 = stockService.getStocksForProduct(1);

        //Assertions
        assertThat(stockLoc1).size().isEqualTo(allProducts.size());   //There should be a stock for each product
        assertThat(stockProd1).size().isEqualTo(allLocations.size());   //There should be a stock for each location

    }

    //Test if @ToString(exclude) behaves correctly
    @Test
    public void orderDetailToString(){

        //Order 1 exists because it is created by addMockData component
        OrderDetail od = shopService.getAllDetailsForOrder(1).iterator().next();

        OrderDetail od1 = new OrderDetail();

        od1.setOrderDetailKey(od.getOrderDetailKey());
        od1.setQuantity(od.getQuantity());

        //This should ignore fields 'order' and 'product'
        System.out.println(od.toString());
        //This does not have a referenced 'order' or 'product'
        System.out.println(od1.toString());

        assertThat(od.toString()).isEqualTo(od1.toString());

    }


    @Test
    public void checkBuilderConsistency(){

        customerRepository.save(Customer.builder()
                .firstName("Gabi")
                .lastName("Deen")
                .userName("prrr2")
                .build());

        customerRepository.findAll().forEach(customer -> System.out.println(customer.toString()));

    }

    @Test
    public void checkBuilderAndJpaConsistency(){
        Customer c1 = Customer.builder()
                .firstName("Gabi")
                .lastName("Deen")
                .userName("prrr1")
                .build();
        Customer c2 = Customer.builder()
                .firstName("Gabi")
                .lastName("Deen")
                .userName("prrr2")
                .build();
        List<Customer> customers = Arrays.asList(c1,c2);

        //Both should have id = 0
        customers.forEach(customer -> System.out.println(customer.toString()));

    //    assertThat(c1.getCustomerId()).isEqualTo(c2.getCustomerId()).isEqualTo(0);

        customerRepository.save(customers);

        assertThat(c1.getCustomerId()).isNotEqualTo(c2.getCustomerId()).isNotEqualTo(0);

        //After being persisted both should have incremented IDs (and unique)
        customerRepository.findAll().forEach(customer -> System.out.println(customer.toString()));
    }

    @Test
    public void checkBuilderDefaultWithConstructorBuilder(){

        Product p = Product.builder().build();

        System.out.println(p.toString());

        //Description default is ""
        assertThat(p.getDescription()).isEqualTo("");

    }

}
