package ro.msg.learning.shop.utility.strategy;

import ro.msg.learning.shop.dto.OrderSpecifications;
import ro.msg.learning.shop.dto.ResolvedOrderDetail;
import ro.msg.learning.shop.domain.StockKey;
import ro.msg.learning.shop.domain.Location;
import ro.msg.learning.shop.domain.Stock;
import ro.msg.learning.shop.dto.ShoppingCartEntry;

import java.util.List;
import java.util.Map;

public class GreedyAlgorithm implements StrategySelectionAlgorithm {
    @Override
    public List<ResolvedOrderDetail> runStrategy(List<ShoppingCartEntry> wishList, List<Location> locationList, Map<StockKey, Stock> stockMap) {
        return null;
    }
}
