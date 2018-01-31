package ro.msg.learning.shop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
    private ProductService productService;

    public StockService(StockRepository stockRepository, ProductService productService){
        this.stockRepository=stockRepository;
        this.productService=productService;
    }

    public List<Stock> getStocks(){return stockRepository.findAll();}

    public Stock findStock(Location location, Product product){
        return stockRepository.findByStockKey_LocationIdAndStockKey_ProductId(location.getLocationId(),product.getProductId());
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
        productService.getAllProducts().forEach(product -> createStock(location,product));
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

}
