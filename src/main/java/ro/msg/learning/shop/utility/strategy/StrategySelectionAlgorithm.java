package ro.msg.learning.shop.utility.strategy;

import ro.msg.learning.shop.dto.OrderSpecifications;
import ro.msg.learning.shop.dto.ResolvedOrderDetail;
import ro.msg.learning.shop.domain.StockKey;
import ro.msg.learning.shop.domain.Location;
import ro.msg.learning.shop.domain.Stock;

import java.util.List;
import java.util.Map;

public interface StrategySelectionAlgorithm {
    List<ResolvedOrderDetail> runStrategy(OrderSpecifications orderSpecifications, List<Location> locationList, Map<StockKey, Stock> stockMap);
}