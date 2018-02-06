package ro.msg.learning.shop.utility.strategy;

import ro.msg.learning.shop.domain.misc.OrderSpecifications;
import ro.msg.learning.shop.domain.misc.ResolvedOrderDetail;
import ro.msg.learning.shop.domain.tables.Location;
import ro.msg.learning.shop.service.ShopService;
import ro.msg.learning.shop.service.StockService;

import java.util.ArrayList;
import java.util.List;

public class SingleLocationAlgorithm implements StrategySelectionAlgorithm{
    @Override
    public List<ResolvedOrderDetail> runStrategy(OrderSpecifications orderSpecifications, ShopService shopService, StockService stockService) {
        List<ResolvedOrderDetail> resultList = new ArrayList<>();

        //For each location, find if there are enough products to fully satisfy the order requirements
        for(Location location: shopService.getAllLocations()){

            List<ResolvedOrderDetail> candidate = new ArrayList<>();

            orderSpecifications.getShoppingCart().forEach(shoppingCartEntry -> {
                if(shoppingCartEntry.getQuantity() <= stockService.findStock(location.getLocationId(), shoppingCartEntry.getProductId()).getQuantity()){
                    candidate.add(new ResolvedOrderDetail(shoppingCartEntry.getProductId(), shoppingCartEntry.getQuantity(), location.getLocationId()));
                } else {
                    candidate.add(new ResolvedOrderDetail(shoppingCartEntry.getProductId(), -1, location.getLocationId()));
                }
            });

            //Check if candidate list is a valid solution
            if(candidate.stream().noneMatch(resolvedOrderDetail -> (resolvedOrderDetail.getQuantity() < 0))){
                resultList = candidate;
                return resultList;
            }
        }
        return resultList;
    }
}
