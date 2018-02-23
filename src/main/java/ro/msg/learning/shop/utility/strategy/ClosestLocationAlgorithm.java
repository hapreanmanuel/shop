package ro.msg.learning.shop.utility.strategy;


import org.springframework.beans.factory.annotation.Autowired;
import ro.msg.learning.shop.domain.Location;
import ro.msg.learning.shop.domain.Stock;
import ro.msg.learning.shop.domain.StockKey;
import ro.msg.learning.shop.dto.ResolvedOrderDetail;
import ro.msg.learning.shop.dto.StrategyDto;
import ro.msg.learning.shop.utility.distance.DistanceCalculator;

import java.util.List;
import java.util.Map;

public class ClosestLocationAlgorithm extends SingleLocationAlgorithm{

    @Autowired
    private DistanceCalculator distanceCalculator;

    @Override
    public List<ResolvedOrderDetail> runStrategy(StrategyDto details, List<Location> locationList, Map<StockKey, Stock> stockMap) {
        return super.runStrategy(details, distanceCalculator.sortLocationsByDistance(locationList, details.getDeliveryAddress()), stockMap);
    }
}
