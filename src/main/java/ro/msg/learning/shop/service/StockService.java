package ro.msg.learning.shop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.domain.*;
import ro.msg.learning.shop.dto.OrderSpecifications;
import ro.msg.learning.shop.dto.ResolvedOrderDetail;
import ro.msg.learning.shop.exception.StockForLocationExistsException;
import ro.msg.learning.shop.repository.LocationRepository;
import ro.msg.learning.shop.repository.SupplierRepository;
import ro.msg.learning.shop.utility.BeansManager;
import ro.msg.learning.shop.utility.strategy.*;
import ro.msg.learning.shop.repository.StockRepository;

import java.util.*;

@Service
public class StockService implements Injectable{

    private final StockRepository stockRepository;
    private final SupplierRepository supplierRepository;
    private final LocationRepository locationRepository;
    private final StrategySelectionAlgorithm algorithm;

    private ShopService shopService;

    @Autowired
    public StockService(StockRepository stockRepository,
                        SupplierRepository supplierRepository,
                        LocationRepository locationRepository,
                        StrategySelectionAlgorithm algorithm){
        this.stockRepository=stockRepository;
        this.supplierRepository = supplierRepository;
        this.locationRepository = locationRepository;
        this.algorithm=algorithm;
    }

    @Override
    public void inject(BeansManager beansManager) {
        this.shopService = beansManager.getShopService();
    }

    /*
        Repository access
     */
    //Location
    public Location getLocation(int locationId) {return locationRepository.findOne(locationId);}
    public List<Location> getAllLocations() { return locationRepository.findAll();}
    public void addLocation(Location location) { locationRepository.save(location);}
    public void deleteLocation(Location location) { locationRepository.delete(location);}

    //Supplier
    public Supplier getSupplier(int supplierId){return supplierRepository.findOne(supplierId);}
    public List<Supplier> getAllSuppliers() { return supplierRepository.findAll(); }
    public void addSupplier(Supplier supplier) { supplierRepository.save(supplier);}
    public void deleteSupplier(Supplier supplier) { supplierRepository.delete(supplier);}

    //Stocks
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


    /*
        Private section
     */
    private List<Stock> getStocks(){return stockRepository.findAll();}

    private void createStock(Location location, Product product, int quantity){

        //Throw exception if location or product ref. are wrong.

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
        return algorithm.runStrategy(currentOrderSpecifications, getAllLocations(), stocksAsMap());
    }

    public void updateStockForResolvedOrderDetails(List<ResolvedOrderDetail> orderResolution){
        orderResolution.forEach(resolvedOrderDetail -> exportStock(resolvedOrderDetail.getLocationId(), resolvedOrderDetail.getProductId(), resolvedOrderDetail.getQuantity()));
    }

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
