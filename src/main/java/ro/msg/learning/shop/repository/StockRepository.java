package ro.msg.learning.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.msg.learning.shop.domain.misc.StockKey;
import ro.msg.learning.shop.domain.tables.Stock;

public interface StockRepository extends JpaRepository<Stock, StockKey>{
    Stock findByStockKey_LocationIdAndStockKey_ProductId(int locationId, int productId);
}
