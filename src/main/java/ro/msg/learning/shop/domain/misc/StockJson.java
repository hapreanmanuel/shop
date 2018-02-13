package ro.msg.learning.shop.domain.misc;

import lombok.AllArgsConstructor;
import lombok.Data;
import ro.msg.learning.shop.domain.tables.Stock;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Data
public class StockJson {
    private int productId;
    private int locationId;
    private int quantity;
    private static StockJson fromStock(Stock stock) {
        return new StockJson(stock.getProductId(), stock.getLocationId(), stock.getQuantity());
    }

    public static List<StockJson> fromStockList(List<Stock> stocks) {
        List<StockJson> out = new ArrayList<>();
        for (Stock stock : stocks) {
            out.add(fromStock(stock));
        }
        return out;
    }
}