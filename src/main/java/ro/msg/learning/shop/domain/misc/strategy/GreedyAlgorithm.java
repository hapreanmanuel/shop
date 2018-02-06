package ro.msg.learning.shop.domain.misc.strategy;

import ro.msg.learning.shop.domain.misc.OrderSpecifications;
import ro.msg.learning.shop.domain.misc.ResolvedOrderDetail;
import ro.msg.learning.shop.service.ShopService;
import ro.msg.learning.shop.service.StockService;

import java.util.List;

public class GreedyAlgorithm implements StrategySelectionAlgorithm {
    @Override
    public List<ResolvedOrderDetail> runStrategy(OrderSpecifications orderSpecifications, ShopService shopService, StockService stockService) {
        return null;
    }
}
