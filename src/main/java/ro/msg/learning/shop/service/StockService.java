package ro.msg.learning.shop.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.domain.*;
import ro.msg.learning.shop.dto.ShoppingCartEntry;
import ro.msg.learning.shop.dto.StrategyDto;
import ro.msg.learning.shop.exception.*;
import ro.msg.learning.shop.repository.*;
import ro.msg.learning.shop.utility.revenue.RevenueCalculator;
import ro.msg.learning.shop.utility.strategy.*;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class StockService {

    private final StockRepository stockRepository;
    private final LocationRepository locationRepository;
    private final OrderRepository orderRepository;
    private final StrategySelectionAlgorithm algorithm;
    private final RevenueRepository revenueRepository;

    @Autowired
    public StockService(StockRepository stockRepository,
                        LocationRepository locationRepository,
                        OrderRepository orderRepository,
                        StrategySelectionAlgorithm algorithm,
                        RevenueRepository revenueRepository){
        this.stockRepository=stockRepository;
        this.locationRepository = locationRepository;
        this.orderRepository=orderRepository;
        this.algorithm=algorithm;
        this.revenueRepository=revenueRepository;
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
    public void performRevenueCalculations(){
        final Date date = new Date();

        log.info("Starting scheduled job: Daily Revenue Calculation {} "+ date);

        List<Order> orders = orderRepository.findByRevenuedAndStatus(false, Order.Status.COMPLETE);     // get orders not revenued yet
        List<Location> shopLocations = locationRepository.findAll();    // all locations


        List<Revenue> revenues = shopLocations.stream().map(location->
                RevenueCalculator.getRevenueForLocation(location, orders.stream().
                        filter(order-> order.getLocation().equals(location)).collect(Collectors.toList()))).
                collect(Collectors.toList());

        revenueRepository.save(revenues);

        log.info("Finished scheduled job: Daily Revenue Calculation {} "+ date);
    }

    @Transactional
    public Order processOrder(int orderId){
        log.info("Started Processing Order ID: {}",orderId);

        Order order = orderRepository.findOne(orderId);

        if(!order.getStatus().equals(Order.Status.PROCESSING)){
            throw new InvalidRequestException();
        }

        Location location = algorithm.runStrategy(
                new StrategyDto(order.getOrderDetails().stream().map(detail-> new ShoppingCartEntry(detail.getProduct().getProductId(),detail.getQuantity()))
                        .collect(Collectors.toList()), order.getShippingAddress()),
                locationRepository.findAll(),
                stocksAsMap());

        order.setLocation(location);

        updateStockForOrder(order);

        order.setStatus(Order.Status.COMPLETE);

        orderRepository.save(order);

        log.info("Selected Export Location Order ID: {}", location.toString());

        log.info("Successfully processed Order ID: {}", orderId);

        return order;
    }

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

    private void updateStock(int locationId, int productId, int quantity) {
        Stock stockToUpdate = findStock(locationId,productId);
        stockToUpdate.setQuantity(stockToUpdate.getQuantity() + quantity);
        stockRepository.save(stockToUpdate);
    }

    private void exportStock(int locationId, int productId, int quantity){
        updateStock(locationId,productId,-quantity);
    }

    private Map<StockKey, Stock> stocksAsMap() {
        return stockRepository.findAll().stream().collect(Collectors.toMap(Stock::getStockKey, stock -> stock));
    }
}
