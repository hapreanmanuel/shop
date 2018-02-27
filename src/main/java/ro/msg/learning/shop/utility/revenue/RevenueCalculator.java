package ro.msg.learning.shop.utility.revenue;

import ro.msg.learning.shop.domain.Location;
import ro.msg.learning.shop.domain.Order;
import ro.msg.learning.shop.domain.OrderDetail;
import ro.msg.learning.shop.domain.Revenue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class RevenueCalculator {

    private RevenueCalculator(){}

    private static BigDecimal getOrderTotal(Order order){
        Function<OrderDetail, BigDecimal> totalMapper = orderDetail -> orderDetail.getProduct().getPrice().multiply(new BigDecimal(orderDetail.getQuantity()));
        return order.getOrderDetails().stream().map(totalMapper).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public static Revenue getRevenueForLocation(Location location, List<Order> orders){

        List<BigDecimal> totals = new ArrayList<>();
        for(Order o : orders){
            totals.add(getOrderTotal(o));
            o.setRevenued(true);
        }
        BigDecimal total = totals.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        return Revenue.builder().location(location).total(total).build();
    }
}
