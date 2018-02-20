package ro.msg.learning.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ro.msg.learning.shop.domain.Stock;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Data
public class StockDto {
    private int productId;
    private int locationId;
    private int quantity;

    private static StockDto fromStock(Stock stock) {
        return new StockDto(stock.getStockKey().getProductId(), stock.getStockKey().getLocationId(), stock.getQuantity());
    }

    public static List<StockDto> fromStockList(List<Stock> stocks) {
        List<StockDto> out = new ArrayList<>();
        for (Stock stock : stocks) {
            out.add(fromStock(stock));
        }
        return out;
    }
}