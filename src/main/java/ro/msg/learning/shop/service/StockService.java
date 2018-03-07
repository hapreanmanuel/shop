package ro.msg.learning.shop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.domain.*;
import ro.msg.learning.shop.dto.ShoppingCartEntry;
import ro.msg.learning.shop.dto.StrategyDto;
import ro.msg.learning.shop.exception.*;
import ro.msg.learning.shop.repository.*;
import ro.msg.learning.shop.utility.strategy.*;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StockService {

    private final StockRepository stockRepository;
    private final LocationRepository locationRepository;
    private final OrderRepository orderRepository;
    private final StrategySelectionAlgorithm algorithm;

    @Autowired
    public StockService(StockRepository stockRepository,
                        LocationRepository locationRepository,
                        OrderRepository orderRepository,
                        StrategySelectionAlgorithm algorithm){
        this.stockRepository=stockRepository;
        this.locationRepository = locationRepository;
        this.orderRepository=orderRepository;
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
    private Stock findStock(int locationId, int productId){
        return stockRepository.findByStockKey_LocationIdAndStockKey_ProductId(locationId,productId);
    }


    @Transactional
    public Order processOrder(int orderId){

        Order order = orderRepository.findOne(orderId);

        if(!order.getStatus().equals(Order.Status.PROCESSING)){
            throw new InvalidRequestException();
        }

        Location location = algorithm.runStrategy(
                new StrategyDto(order.getOrderDetails().stream().map(detail-> new ShoppingCartEntry(detail.getProduct().getProductId(),detail.getQuantity()))
                        .collect(Collectors.toList()), order.getShippingAddress())
                ,locationRepository.findAll()
                ,stocksAsMap());

        order.setLocation(location);

        if(location==null){
            order.setStatus(Order.Status.DENYED);
        }
        else{
            updateStockForOrder(order);
            order.setStatus(Order.Status.COMPLETE);
        }

        orderRepository.save(order);

        return order;
    }

    @Transactional
    private void updateStockForOrder(Order order){
        order.getOrderDetails().forEach(orderDetail -> exportStock(order.getLocation().getLocationId(),
                orderDetail.getProduct().getProductId(),
                orderDetail.getQuantity()));
    }

    public List<Stock> getExportStock(int orderId){

        Order order = orderRepository.findOne(orderId);

        List<Stock> exportStocks = new ArrayList<>();

        order.getOrderDetails().forEach(orderDetail -> exportStocks.add(Stock.builder()
                .product(orderDetail.getProduct())
                .location(order.getLocation())
                .stockKey(new StockKey(orderDetail.getProduct().getProductId(),order.getLocation().getLocationId()))
                .quantity(orderDetail.getQuantity())
                .build()));
        return exportStocks;
    }

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
