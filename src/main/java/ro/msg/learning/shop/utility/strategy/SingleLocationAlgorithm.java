package ro.msg.learning.shop.utility.strategy;

import ro.msg.learning.shop.dto.OrderSpecifications;
import ro.msg.learning.shop.dto.ResolvedOrderDetail;
import ro.msg.learning.shop.domain.StockKey;
import ro.msg.learning.shop.domain.Location;
import ro.msg.learning.shop.domain.Stock;
import ro.msg.learning.shop.dto.ShoppingCartEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/*
    Single Location Strategy

    Find a single location that has all the required products (with the required quantities) in stock.
    If there are more such locations, simply take the first one based on the ID.

 */

public class SingleLocationAlgorithm implements StrategySelectionAlgorithm{
    @Override
    public List<ResolvedOrderDetail> runStrategy(List<ShoppingCartEntry> wishList, List<Location> locationList, Map<StockKey, Stock> stockMap) {

        //For each location, find if there are enough products to fully satisfy the order requirements
        for(Location location: locationList){

            List<ResolvedOrderDetail> candidate = new ArrayList<>();

            wishList.forEach(shoppingCartEntry -> {
                if(shoppingCartEntry.getQuantity() <= stockMap.get(new StockKey(shoppingCartEntry.getProductId(),location.getLocationId())).getQuantity()){
                    candidate.add(new ResolvedOrderDetail(shoppingCartEntry.getProductId(), shoppingCartEntry.getQuantity(), location.getLocationId()));
                }else {
                    candidate.add(new ResolvedOrderDetail(shoppingCartEntry.getProductId(), -1, location.getLocationId()));
                }
            });

            //Check if candidate list is a valid solution
            if(candidate.stream().noneMatch(resolvedOrderDetail -> (resolvedOrderDetail.getQuantity() < 0))){
                return candidate;
            }
        }
        return new ArrayList<>();
    }
}
