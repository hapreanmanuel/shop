package ro.msg.learning.shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ro.msg.learning.shop.domain.tables.Location;
import ro.msg.learning.shop.domain.tables.Stock;
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

    @GetMapping("/locations")
    public @ResponseBody List<Location> getLocations() {
        return stockService.getAllLocations();
    }

    @GetMapping("/{locationId}")
    public @ResponseBody List<Stock> getStocksForLocation(@PathVariable("locationId") int locationId){
        return stockService.getStocksForLocation(locationId);
    }

    @GetMapping(
            value = "/export/{orderId}",
            produces = "text/csv")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody List<Stock> getExportStockForOrderAsCsv(@PathVariable("orderId") int orderId){
        return stockService.getExportStock(shopService.getOrder(orderId));
    }

}
