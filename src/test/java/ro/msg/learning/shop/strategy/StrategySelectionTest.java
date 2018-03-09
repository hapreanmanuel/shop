package ro.msg.learning.shop.strategy;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ro.msg.learning.shop.domain.Address;
import ro.msg.learning.shop.domain.Location;
import ro.msg.learning.shop.domain.Stock;
import ro.msg.learning.shop.domain.StockKey;
import ro.msg.learning.shop.dto.ShoppingCartEntry;
import ro.msg.learning.shop.dto.StrategyDto;
import ro.msg.learning.shop.exception.InsufficientStocksException;
import ro.msg.learning.shop.repository.LocationRepository;
import ro.msg.learning.shop.repository.StockRepository;
import ro.msg.learning.shop.utility.distance.DistanceCalculator;
import ro.msg.learning.shop.utility.strategy.ClosestLocationAlgorithm;
import ro.msg.learning.shop.utility.strategy.SingleLocationAlgorithm;
import ro.msg.learning.shop.utility.strategy.StrategySelectionAlgorithm;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Java6Assertions.assertThat;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class StrategySelectionTest {

    //Algorithm
    private StrategySelectionAlgorithm algorithm;

    //Required parameters
    private List<Location> shopLocations;
    private Map<StockKey, Stock> stockMap;

    //Test locations
    private Location mainLocation;
    private Location secondaryLocation;

    //Customer parameters
    private Address address;
    private List<ShoppingCartEntry> wishList;

    private Location result;

    //Required beans
    @Autowired
    private StockRepository stockRepository;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private DistanceCalculator distanceCalculator;

    @Before
    public void init(){
        shopLocations = locationRepository.findAll();
        stockMap = stockRepository.findAll().stream().collect(Collectors.toMap(Stock::getStockKey, stock -> stock));
        mainLocation = shopLocations.get(0);
        secondaryLocation = shopLocations.get(1);
    }

    @Test
    public void strategySelectionException(){
        /*
            If no suitable strategy is found, the algorithm is expected to throw an
            'InsufficientStocksException'
         */
        log.info("=========================================================================");
        log.info("Location Strategy Test: Insufficient stocks ");

        algorithm = new SingleLocationAlgorithm();

        wishList = Arrays.asList(
                new ShoppingCartEntry(1,4),
                new ShoppingCartEntry(2,304)
        );

        address = Address.builder()
                .region("AB").country("Romania").city("Sebes")
                .fullAddress("Str. Spicului 14").build();

        try{
            result = algorithm.runStrategy(
                new StrategyDto(wishList,address),
                shopLocations,
                stockMap);
        }catch (Exception e){
            log.info("Intercepted exception message: {}", e.getMessage());
            assertThat(e).isInstanceOf(InsufficientStocksException.class);
        }
    }

    @Test
    public void singleLocationTest(){
        log.info("=========================================================================");
        log.info("Location Strategy Test: Single Location Algorithm ");

        algorithm = new SingleLocationAlgorithm();

        wishList = Arrays.asList(
                new ShoppingCartEntry(1,4),
                new ShoppingCartEntry(2,4)
        );

        address = Address.builder()
                .region("AB").country("Romania").city("Sebes")
                .fullAddress("Str. Spicului 14").build();

        result = algorithm.runStrategy(
                new StrategyDto(wishList,address),
                shopLocations,
                stockMap);

        log.info("Request: {}", wishList.toString());
        log.info("Expected location: {}; Found location: {}", mainLocation.getName(),result.getName());

        assertThat(result.getName()).isEqualTo(mainLocation.getName());

        wishList = Arrays.asList(
                new ShoppingCartEntry(1,4),
                new ShoppingCartEntry(2,110)
        );

        result = algorithm.runStrategy(
                new StrategyDto(wishList,address),
                shopLocations,
                stockMap);

        log.info("Request: {}", wishList.toString());
        log.info("Expected location: {}; Found location: {}", secondaryLocation.getName(),result.getName());

        assertThat(result.getName()).isEqualTo(secondaryLocation.getName());

        log.info("=========================================================================");
    }

    @Test
    public void closestLocationTest(){
        log.info("=========================================================================");
        log.info("Location Strategy Test: Closest Location Algorithm ");

        algorithm = new ClosestLocationAlgorithm(distanceCalculator);

        wishList = Arrays.asList(
                new ShoppingCartEntry(1,4),
                new ShoppingCartEntry(2,4)
        );

        address = Address.builder()
                .region("SM").country("Romania").city("Satu Mare")
                .fullAddress("Str. Spicului 14").build();

        result = algorithm.runStrategy(
                new StrategyDto(wishList,address),
                shopLocations,
                stockMap);

        log.info("Delivery address: {}", address.toString());
        log.info("Expected location: {}; Found location: {}", mainLocation, result);

        assertThat(result).isEqualTo(mainLocation);

        address = Address.builder()
                .region("CT").country("Romania").city("Constanta")
                .fullAddress("Str. Spicului 14").build();

        result = algorithm.runStrategy(
                new StrategyDto(wishList,address),
                shopLocations,
                stockMap);

        log.info("Delivery address: {}", address.toString());
        log.info("Expected location: {}; Found location: {}", secondaryLocation, result);

        assertThat(result).isEqualTo(secondaryLocation);

        log.info("=========================================================================");
    }
}
