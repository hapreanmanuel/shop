package ro.msg.learning.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.msg.learning.shop.domain.tables.Stock;

import java.util.List;

public interface StockRepository extends JpaRepository<Stock, Integer>{

    Stock findByStockKey_LocationIdAndStockKey_ProductId(int locationId, int productId);

    List<Stock> findAllByLocation_LocationId(int locationId);

}
