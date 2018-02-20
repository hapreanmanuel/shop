package ro.msg.learning.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.msg.learning.shop.domain.StockKey;
import ro.msg.learning.shop.domain.Stock;

import java.util.List;

public interface StockRepository extends JpaRepository<Stock, StockKey>{
    Stock findByStockKey_LocationIdAndStockKey_ProductId(int locationId, int productId);

    List<Stock> findByStockKey_LocationId(int locationId);

    List<Stock> findByStockKey_ProductId(int productId);

}
