package ro.msg.learning.shop.utility.strategy;

import ro.msg.learning.shop.domain.Location;
import ro.msg.learning.shop.domain.Stock;
import ro.msg.learning.shop.domain.StockKey;
import ro.msg.learning.shop.dto.StrategyDto;
import ro.msg.learning.shop.utility.distance.DistanceCalculator;

import java.util.List;
import java.util.Map;

public class ClosestLocationAlgorithm extends SingleLocationAlgorithm{

    private DistanceCalculator distanceCalculator;

    public ClosestLocationAlgorithm(DistanceCalculator distanceCalculator){
        this.distanceCalculator=distanceCalculator;
    }

    @Override
    public Location runStrategy(StrategyDto details, List<Location> locationList, Map<StockKey, Stock> stockMap) {
        return super.runStrategy(details, distanceCalculator.sortLocationsByDistance(locationList, details.getDeliveryAddress()), stockMap);
    }
}
