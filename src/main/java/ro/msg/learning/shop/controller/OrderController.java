package ro.msg.learning.shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ro.msg.learning.shop.domain.misc.OrderSpecifications;
import ro.msg.learning.shop.domain.tables.Order;
import ro.msg.learning.shop.service.OrderService;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController{

    @Autowired
    private OrderService orderService;

    @GetMapping("/{customerId}/all")
    public @ResponseBody List<Order> getOrdersForCustomer(@PathVariable("customerId") int customerId){
        return orderService.getAllOrdesForCustomer(customerId);
    }

    @PostMapping(
            value = "/{customerId}/createorder",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Order createOrder(
            @PathVariable("customerId") int customerId,
            @RequestBody OrderSpecifications orderSpecifications){
        return orderService.createNewOrder(orderSpecifications);
    }
}
