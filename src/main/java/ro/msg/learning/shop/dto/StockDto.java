package ro.msg.learning.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ro.msg.learning.shop.domain.Stock;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Data
public class StockDto {
    private int productId;
    private int locationId;
    private int quantity;

    public static List<StockDto> fromStockList(List<Stock> stocks) {
        return stocks.stream().map(stock-> new StockDto(stock.getStockKey().getProductId(), stock.getStockKey().getLocationId(), stock.getQuantity())).collect(Collectors.toList());
    }
}