package ro.msg.learning.shop.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import ro.msg.learning.shop.service.StockService;

@Configuration
@EnableScheduling
public class RevenueConfig {

    private final StockService stockService;

    public RevenueConfig(StockService stockService) {
        this.stockService = stockService;
    }

    @Scheduled(cron = "0 5 9 * * *")
    public void createDailyRevenues() {
        stockService.performRevenueCalculations();
    }
}
