package ro.msg.learning.shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ro.msg.learning.shop.domain.misc.StockJson;
import ro.msg.learning.shop.domain.tables.Location;
import ro.msg.learning.shop.service.ShopService;
import ro.msg.learning.shop.service.StockService;

import java.util.List;

@RestController
@RequestMapping("/stock")
public class StockController {

    @Autowired
    private StockService stockService;

    @Autowired
    private ShopService shopService;

    @GetMapping(value = "/locations",
                produces = "application/json")
    public @ResponseBody List<Location> getLocations() {
        return stockService.getAllLocations();
    }

    @GetMapping(path= "/{locationId}",
            produces = "text/csv")
    public List<StockJson> getStocksForLocation(@PathVariable("locationId") int locationId){
        return StockJson.fromStockList(stockService.getStocksForLocation(locationId));
    }

    @GetMapping(
            value = "/export/{orderId}",
            produces = "text/csv")
    @ResponseStatus(HttpStatus.OK)
    public List<StockJson> getExportStockForOrderAsCsv(@PathVariable("orderId") int orderId){
        return StockJson.fromStockList(stockService.getExportStock(shopService.getOrder(orderId)));
    }

}


