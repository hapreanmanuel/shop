package ro.msg.learning.shop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.domain.misc.OrderSpecifications;
import ro.msg.learning.shop.domain.misc.ResolvedOrderDetail;
import ro.msg.learning.shop.domain.misc.StockKey;
import ro.msg.learning.shop.domain.tables.Order;
import ro.msg.learning.shop.exceptions.StockForLocationExistsException;
import ro.msg.learning.shop.utility.strategy.*;
import ro.msg.learning.shop.domain.tables.Location;
import ro.msg.learning.shop.domain.tables.Product;
import ro.msg.learning.shop.domain.tables.Stock;
import ro.msg.learning.shop.repository.StockRepository;

import java.util.*;

@Service
public class StockService {

    private final StockRepository stockRepository;

    private final ShopService shopService;

    private final StrategySelectionAlgorithm algorithm;

    @Autowired
    public StockService(StockRepository stockRepository, ShopService shopService, StrategySelectionAlgorithm algorithm){
        this.stockRepository=stockRepository;
        this.shopService=shopService;
        this.algorithm=algorithm;
    }

    /*
        Private section
     */
    private List<Stock> getStocks(){return stockRepository.findAll();}

    //Create stock for location
    private void createStock(Location location, Product product, int quantity){

        //Throw exceptions if location or product ref. are wrong.

        //Throw exception if the stock already exists because the method to use should be 'updateStock'
        if(findStock(location.getLocationId(),product.getProductId()) != null) throw new StockForLocationExistsException();

        Stock stockToCreate = new Stock();
        stockToCreate.setStockKey(new StockKey(product.getProductId(), location.getLocationId()));
        stockToCreate.setLocation(location);
        stockToCreate.setProduct(product);
        stockToCreate.setQuantity(quantity);           // default quantity
        stockRepository.save(stockToCreate);
    }

    //Update stocks -> We do not need a reference to the actual location or product because they have been assigned when the stock was created
    private void updateStock(int locationId, int productId, int quantity) {
        Stock stockToUpdate = findStock(locationId,productId);
        stockToUpdate.setQuantity(stockToUpdate.getQuantity() + quantity);
        stockRepository.save(stockToUpdate);
    }


    /*
        Public section
     */
    public List<Stock> getStocksForLocation(int locationId){ return stockRepository.findByStockKey_LocationId(locationId);}

    public List<Stock> getStocksForProduct(int productId) {return stockRepository.findByStockKey_ProductId(productId);}

    public Stock findStock(int locationId, int productId){
        return stockRepository.findByStockKey_LocationIdAndStockKey_ProductId(locationId,productId);
    }

    //Create empty stocks for all products for a location
    public void createEmptyStocksForLocation(Location location){
        shopService.getAllProducts().forEach(product -> createStock(location,product, 0));
    }

    //Create stocks with quantity for all products for a location
    public void createStocksForLocation(Location location, int quantity){
        shopService.getAllProducts().forEach(product -> createStock(location,product ,quantity ));
    }

    //Import stock
    public void importStock(int locationId, int productId, int quantity) {
        updateStock(locationId,productId,quantity);
    }

    //Export stock
    private void exportStock(int locationId, int productId, int quantity){
        updateStock(locationId,productId,-quantity);
    }

    private Map<StockKey, Stock> stocksAsMap(){
        Map<StockKey, Stock> stockMap = new HashMap<>();
        getStocks().forEach(stock -> stockMap.put(stock.getStockKey(), stock));
        return stockMap;
    }

    /*
        Selection Strategy

        Runs a distribution algorithm based on property 'shop.strategy'
            from the application.properties file
     */

    public List<ResolvedOrderDetail> getStrategy(OrderSpecifications currentOrderSpecifications){
        return algorithm.runStrategy(currentOrderSpecifications, shopService.getAllLocations(), stocksAsMap());
    }

    public void updateStockForResolvedOrderDetails(List<ResolvedOrderDetail> orderResolution){
        orderResolution.forEach(resolvedOrderDetail -> exportStock(resolvedOrderDetail.getLocationId(), resolvedOrderDetail.getProductId(), resolvedOrderDetail.getQuantity()));
    }

    //TODO - is this needed?
    //These entities are created on request and are not persisted in the database
    public List<Stock> getExportStock(Order order){
        List<Stock> exportStocks = new ArrayList<>();

        order.getOrderDetails().forEach(orderDetail -> {
            Stock stock = new Stock();
            stock.setProduct(orderDetail.getProduct());
            stock.setLocation(order.getLocation());
            stock.setStockKey(new StockKey(orderDetail.getProduct().getProductId(),order.getLocation().getLocationId()));
            stock.setQuantity(orderDetail.getQuantity());
            exportStocks.add(stock);
        });
        return exportStocks;
    }

}
