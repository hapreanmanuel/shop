package ro.msg.learning.shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ro.msg.learning.shop.domain.Customer;
import ro.msg.learning.shop.domain.Order;
import ro.msg.learning.shop.dto.OrderCreationDto;
import ro.msg.learning.shop.dto.OrderSpecifications;
import ro.msg.learning.shop.domain.Product;
import ro.msg.learning.shop.service.ShopService;
import ro.msg.learning.shop.service.StockService;

import java.util.List;

/*
    Main controller class for shop application
 */
@RestController
@RequestMapping("/shop")
public class ShopController {

    private ShopService shopService;
    private StockService stockService;

    @Autowired
    public ShopController(ShopService shopService, StockService stockService) {
        this.shopService = shopService;
        this.stockService = stockService;
    }

    @GetMapping(value = "/products",
                produces = "application/json")
    public @ResponseBody List<Product> getAllProducts(){
        return shopService.getAllProducts();
    }

    @GetMapping(value = "/customers",
                produces = "application/json")
    public @ResponseBody List<Customer> getCustomers() { return shopService.getAllCustomers(); }


    @PostMapping(
            value = "/orders/create",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = "application/json")
    public @ResponseBody Order createOrder(@RequestBody OrderCreationDto request, Authentication authentication){
        OrderSpecifications os = shopService.createOrderSpecifications(request, authentication.getName());
        return shopService.createNewOrder(os);
    }

    @PostMapping(
            value="/orders/process/{orderId}",
            produces = "application/json")
    public @ResponseBody Order processOrder(@PathVariable int orderId){
        return stockService.processOrder(orderId);
    }
}

