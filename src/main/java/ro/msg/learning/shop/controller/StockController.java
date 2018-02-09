package ro.msg.learning.shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ro.msg.learning.shop.domain.tables.Stock;
import ro.msg.learning.shop.service.StockService;

import java.util.List;

@RestController
@RequestMapping("/stocks")
public class StockController {

    @Autowired
    private StockService stockService;

    //TODO - make response as CSV
    @GetMapping("/{locationId}")
    public @ResponseBody List<Stock> getStocksForLocation(@PathVariable("locationId") int locationId){
        return stockService.getStocksForLocation(locationId);
    }
}
