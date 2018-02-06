package ro.msg.learning.shop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.domain.misc.OrderSpecifications;
import ro.msg.learning.shop.domain.misc.ResolvedOrderDetail;
import ro.msg.learning.shop.domain.misc.strategy.GreedyAlgorithm;
import ro.msg.learning.shop.domain.misc.strategy.MostAbundantAlgorithm;
import ro.msg.learning.shop.domain.misc.strategy.SingleLocationAlgorithm;
import ro.msg.learning.shop.domain.misc.strategy.StrategySelectionAlgorithm;
import ro.msg.learning.shop.domain.tables.Location;
import ro.msg.learning.shop.domain.tables.Product;
import ro.msg.learning.shop.domain.tables.Stock;
import ro.msg.learning.shop.repository.StockRepository;

import java.util.List;

@Service
public class StockService {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private ShopService shopService;

    @Value("${shop.stock.strategy}")
    private String strategyName;

    public StockService(StockRepository stockRepository, ShopService shopService){
        this.stockRepository=stockRepository;
        this.shopService=shopService;
    }

    public List<Stock> getStocks(){return stockRepository.findAll();}

    public Stock findStock(Location location, Product product){
        return stockRepository.findByStockKey_LocationIdAndStockKey_ProductId(location.getLocationId(),product.getProductId());
    }
    public Stock findStock(int locationId, int productId){
        return stockRepository.findByStockKey_LocationIdAndStockKey_ProductId(locationId,productId);
    }

    //Create stock for location
    public void createStock(Location location, Product product) throws RuntimeException{

        //Throw exceptions if location or product ref. are wrong.

        //Throw exception if the stock already exists because the method to use should be 'updateStock'
        if(findStock(location,product) != null){
            //TODO - define custom exceptions
            throw new RuntimeException("Stock allready exists for given product at that location");
        }
        stockRepository.save(new Stock(location,product));
    }

    //Create stocks for all products for a location
    public void createStocksForLocation(Location location){
        shopService.getAllProducts().forEach(product -> createStock(location,product));
    }

    //Update stocks
    private void updateStock(Location location, Product product, int quantity) {
        Stock stockToUpdate = findStock(location,product);
        stockToUpdate.setQuantity(stockToUpdate.getQuantity() + quantity);
        stockRepository.save(stockToUpdate);
    }

    //Import stocks
    public void importStock(Location location, Product product, int quantity) {
        updateStock(location,product,quantity);
    }

    //Export stocks
    public void exportStock(Location location, Product product, int quantity){
        updateStock(location,product,-quantity);
    }

    /*
        Selection Strategy

        Runs a distribution algorithm based on property 'shop.stock.strategy'
            from the application.properties file

     */
    public List<ResolvedOrderDetail> getStrategy(OrderSpecifications currentOrderSpecifications){

        StrategySelectionAlgorithm strategy;

        switch(strategyName){
            case "single":{
                strategy=new SingleLocationAlgorithm();
                break;
            }
            case "abundant":{
                strategy= new MostAbundantAlgorithm();
                break;
            }
            case "greedy": {
                strategy = new GreedyAlgorithm();
                break;
            }
            default: strategy = new SingleLocationAlgorithm();
        }
        return strategy.runStrategy(currentOrderSpecifications, shopService, this );
    }

}
