package ro.msg.learning.shop.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import ro.msg.learning.shop.domain.Location;
import ro.msg.learning.shop.domain.Order;
import ro.msg.learning.shop.repository.LocationRepository;
import ro.msg.learning.shop.repository.OrderRepository;
import ro.msg.learning.shop.repository.RevenueRepository;
import ro.msg.learning.shop.utility.revenue.RevenueCalculator;

import javax.transaction.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableScheduling
@Slf4j
public class RevenueConfig {
    private final RevenueRepository revenueRepository;
    private final OrderRepository orderRepository;
    private final LocationRepository locationRepository;

    @Autowired
    public RevenueConfig(RevenueRepository revenueRepository, OrderRepository orderRepository, LocationRepository locationRepository) {
        this.revenueRepository = revenueRepository;
        this.orderRepository = orderRepository;
        this.locationRepository = locationRepository;
    }

    @Transactional
    @Scheduled(cron = "0 0 22 * * *")
    public void createDailyRevenues() {

        log.info("Starting scheduled job: Daily Revenue Calculation {} "+ new Date());

        List<Order> orders = orderRepository.findByRevenued(false);     // get orders not revenued yet
        List<Location> shopLocations = locationRepository.findAll();    // all locations

        //Create a revenue entry for each shop location
        shopLocations.forEach(location -> {
            List<Order> locationOrders = orders.stream().filter(order -> order.getLocation().equals(location)).collect(Collectors.toList());
            revenueRepository.save(RevenueCalculator.getRevenueForLocation(location, locationOrders));
            orderRepository.save(locationOrders);       //update 'revenued' field to true
        });

        log.info("Finished scheduled job: Daily Revenue Calculation {} "+ new Date());
    }

}
