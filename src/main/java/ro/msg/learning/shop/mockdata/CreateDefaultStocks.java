package ro.msg.learning.shop.mockdata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import ro.msg.learning.shop.service.StockService;

import java.util.Arrays;
import java.util.List;

/*
    Create default stocks for the 2 mock locations
    - uses logic from StockService
    - 100 pieces from each product in the 1st location
    - 200 pieces from each product in the 2nd location
 */
@Component
public class CreateDefaultStocks implements CommandLineRunner, Ordered {
    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE-1;
    }

    private final List<Integer> stockSizeForLocations = Arrays.asList(100,200);
    private final StockService stockService;

    @Autowired
    public CreateDefaultStocks(StockService stockService) {
        this.stockService = stockService;
    }

    @Override
    public void run(String... args) {
        stockService.createStocksForLocation(stockService.getAllLocations().get(0),stockSizeForLocations.get(0));
        stockService.createStocksForLocation(stockService.getAllLocations().get(1),stockSizeForLocations.get(1));
    }
}
