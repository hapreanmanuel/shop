package ro.msg.learning.shop.utility.strategy;

import ro.msg.learning.shop.domain.misc.OrderSpecifications;
import ro.msg.learning.shop.domain.misc.ResolvedOrderDetail;
import ro.msg.learning.shop.domain.misc.StockKey;
import ro.msg.learning.shop.domain.tables.Location;
import ro.msg.learning.shop.domain.tables.Stock;

import java.util.List;
import java.util.Map;

public interface StrategySelectionAlgorithm {
    List<ResolvedOrderDetail> runStrategy(OrderSpecifications orderSpecifications, List<Location> locationList, Map<StockKey, Stock> stockMap);
}
