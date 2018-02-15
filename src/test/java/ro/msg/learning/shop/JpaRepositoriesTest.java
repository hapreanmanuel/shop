package ro.msg.learning.shop;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ro.msg.learning.shop.domain.Location;
import ro.msg.learning.shop.domain.OrderDetail;
import ro.msg.learning.shop.domain.Product;
import ro.msg.learning.shop.domain.Stock;
import ro.msg.learning.shop.service.ShopService;
import ro.msg.learning.shop.service.StockService;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JpaRepositoriesTest {

    @Autowired
    private StockService stockService;
    @Autowired
    private ShopService shopService;

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


}
