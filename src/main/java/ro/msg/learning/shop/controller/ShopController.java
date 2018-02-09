package ro.msg.learning.shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ro.msg.learning.shop.domain.tables.*;
import ro.msg.learning.shop.service.ShopService;

import java.util.List;

/*
    Main controller class for shop application
 */
@RestController
@RequestMapping("/shop")
public class ShopController {

    @Autowired
    private ShopService shopService;

    @GetMapping("/products")
    public @ResponseBody List<Product> getAllProducts(){
        return shopService.getAllProducts();
    }

    @GetMapping("/customers")
    public @ResponseBody List<Customer> getCustomers() { return shopService.getAllCustomers(); }

    @GetMapping("/locations")
    public @ResponseBody List<Location> getLocations() {
        return shopService.getAllLocations();
    }

    @GetMapping("/customers/{customerId}")
    public @ResponseBody Customer getCustomer(@PathVariable("customerId") int customerId){
        return shopService.getCustomer(customerId);
    }

}
