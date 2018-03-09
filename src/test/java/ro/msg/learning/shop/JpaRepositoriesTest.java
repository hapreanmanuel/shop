package ro.msg.learning.shop;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ro.msg.learning.shop.domain.*;
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

    @Test
    public void checkBuilderDefaultWithConstructorBuilder(){

        Product p = Product.builder().build();

        System.out.println(p.toString());

        //Description default is ""
        assertThat(p.getDescription()).isEqualTo("");
    }
}
