package ro.msg.learning.shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ro.msg.learning.shop.domain.Location;
import ro.msg.learning.shop.dto.StockDto;
import ro.msg.learning.shop.service.ShopService;
import ro.msg.learning.shop.service.StockService;

import java.util.List;

@RestController
@RequestMapping("/stock")
public class StockController {

    private StockService stockService;
    private ShopService shopService;

    @Autowired
    public StockController(StockService stockService, ShopService shopService) {
        this.stockService = stockService;
        this.shopService = shopService;
    }

    @GetMapping(value = "/locations",
                produces = "application/json")
    public @ResponseBody List<Location> getLocations() {
        return stockService.getAllLocations();
    }

    @GetMapping(path= "/{locationId}",
            produces = "text/csv")
    public List<StockDto> getStocksForLocation(@PathVariable("locationId") int locationId){
        return StockDto.fromStockList(stockService.getStocksForLocation(locationId));
    }

    @GetMapping(
            value = "/export/{orderId}",
            produces = "text/csv")
    @ResponseStatus(HttpStatus.OK)
    public List<StockDto> getExportStockForOrderAsCsv(@PathVariable("orderId") int orderId){
        return StockDto.fromStockList(stockService.getExportStock(shopService.getOrder(orderId)));
    }

}


