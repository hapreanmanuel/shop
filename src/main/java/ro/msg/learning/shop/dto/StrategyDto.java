package ro.msg.learning.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ro.msg.learning.shop.domain.Address;

import java.util.List;

@AllArgsConstructor
@Data
public class StrategyDto {
    private List<ShoppingCartEntry> wishList;
    private Address deliveryAddress;
}
