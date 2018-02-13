package ro.msg.learning.shop.utility;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import ro.msg.learning.shop.service.Injectable;
import ro.msg.learning.shop.service.ShopService;
import ro.msg.learning.shop.service.StockService;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

@Component
public class BeansManager{

    @Autowired
    private ShopService shopService;

    @Autowired
    private StockService stockService;

    @Autowired
    private final Set<Injectable> injectables = new HashSet();

    @PostConstruct
    private void inject(){
        for(Injectable injectableItem: injectables){
            injectableItem.inject(this);
        }
    }

    public ShopService getShopService() {
        return shopService;
    }

    public void setShopService(ShopService shopService) {
        this.shopService = shopService;
    }

    public StockService getStockService() {
        return stockService;
    }

    public void setStockService(StockService stockService) {
        this.stockService = stockService;
    }
}