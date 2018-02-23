package ro.msg.learning.shop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.domain.*;
import ro.msg.learning.shop.dto.OrderSpecifications;
import ro.msg.learning.shop.dto.StrategyDto;
import ro.msg.learning.shop.exception.EmptyShoppingCartException;
import ro.msg.learning.shop.exception.InvalidProductException;
import ro.msg.learning.shop.exception.InvalidShopLocationException;
import ro.msg.learning.shop.exception.InvalidStockCreationException;
import ro.msg.learning.shop.repository.LocationRepository;
import ro.msg.learning.shop.repository.ProductRepository;
import ro.msg.learning.shop.repository.SupplierRepository;
import ro.msg.learning.shop.utility.strategy.*;
import ro.msg.learning.shop.repository.StockRepository;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StockService {

    private final StockRepository stockRepository;
    private final SupplierRepository supplierRepository;
    private final LocationRepository locationRepository;
    private final ProductRepository productRepository;
    private final StrategySelectionAlgorithm algorithm;

    @Autowired
    public StockService(StockRepository stockRepository,
                        SupplierRepository supplierRepository,
                        LocationRepository locationRepository,
                        ProductRepository productRepository,
                        StrategySelectionAlgorithm algorithm){
        this.stockRepository=stockRepository;
        this.supplierRepository = supplierRepository;
        this.locationRepository = locationRepository;
        this.productRepository = productRepository;
        this.algorithm=algorithm;
    }

    /*
        Repository access
     */
    //Location
    public List<Location> getAllLocations() { return locationRepository.findAll();}

    //Stocks
    public List<Stock> getStocksForLocation(int locationId){ return stockRepository.findByStockKey_LocationId(locationId);}
    public List<Stock> getStocksForProduct(int productId) {return stockRepository.findByStockKey_ProductId(productId);}
    public Stock findStock(int locationId, int productId){
        return stockRepository.findByStockKey_LocationIdAndStockKey_ProductId(locationId,productId);
    }

    /*
        Business logic
     */

    //Create stocks with quantity for all products for a location
    public void createStocksForLocation(Location location, int quantity){
        productRepository.findAll().forEach(product -> createStock(location,product ,quantity ));
    }

    /*
        Selection Strategy

        Runs a distribution algorithm based on property 'shop.strategy'
            from the application.properties file
     */

    @Transactional
    public OrderSpecifications processRequest(OrderSpecifications orderSpecifications){

        if(orderSpecifications.getShoppingCart().isEmpty()){
            throw new EmptyShoppingCartException();
        }
        /*
            Run selection strategy
         */
        orderSpecifications.setResolution(algorithm.runStrategy(
                new StrategyDto(orderSpecifications.getShoppingCart(), orderSpecifications.getAddress()),
                locationRepository.findAll(),
                stocksAsMap()));
        return orderSpecifications;
    }

    @Transactional
    public void updateStockForOrder(Order order){
        order.getOrderDetails().forEach(orderDetail -> exportStock(order.getLocation().getLocationId(),
                orderDetail.getProduct().getProductId(),
                orderDetail.getQuantity()));
    }

    //These entities are created on request and are not persisted in the database
    public List<Stock> getExportStock(Order order){
        List<Stock> exportStocks = new ArrayList<>();

        order.getOrderDetails().forEach(orderDetail -> exportStocks.add(Stock.builder()
                .product(orderDetail.getProduct())
                .location(order.getLocation())
                .stockKey(new StockKey(orderDetail.getProduct().getProductId(),order.getLocation().getLocationId()))
                .quantity(orderDetail.getQuantity())
                .build()));
        return exportStocks;
    }

    /*
        Private section
     */

    @Transactional
    private void createStock(Location location, Product product, int quantity){

        //Throw exception if location or product ref. are wrong.
        if(!productRepository.exists(product.getProductId())){
            throw new InvalidProductException();
        }
        if(!locationRepository.exists(location.getLocationId())){
            throw new InvalidShopLocationException();
        }
        //Throw exception if the stock already exists because the method to use should be 'updateStock'
        if(stockRepository.exists(new StockKey(product.getProductId(),location.getLocationId()))){
            throw new InvalidStockCreationException();
        }

        stockRepository.save(Stock.builder()
                .stockKey(new StockKey(product.getProductId(), location.getLocationId()))
                .location(location)
                .product(product)
                .quantity(quantity)
                .build());
    }

    //Update stocks -> We do not need a reference to the actual location or product because they have been assigned when the stock was created
    @Transactional
    private void updateStock(int locationId, int productId, int quantity) {
        Stock stockToUpdate = findStock(locationId,productId);
        stockToUpdate.setQuantity(stockToUpdate.getQuantity() + quantity);
        stockRepository.save(stockToUpdate);
    }

    //Export stock
    private void exportStock(int locationId, int productId, int quantity){
        updateStock(locationId,productId,-quantity);
    }

    private Map<StockKey, Stock> stocksAsMap() {
        return stockRepository.findAll().stream().collect(Collectors.toMap(Stock::getStockKey, stock -> stock));
    }
}
