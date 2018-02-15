package ro.msg.learning.shop.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ro.msg.learning.shop.utility.strategy.GreedyAlgorithm;
import ro.msg.learning.shop.utility.strategy.MostAbundantAlgorithm;
import ro.msg.learning.shop.utility.strategy.SingleLocationAlgorithm;
import ro.msg.learning.shop.utility.strategy.StrategySelectionAlgorithm;

@Configuration
public class StrategyConfig {

    public enum Strategy {
        SINGLE,
        ABUNDANT,
        GREEDY,
    }

    @Bean
    public StrategySelectionAlgorithm getAlgorithm(@Value("${shop.strategy}") Strategy strategy){

        switch(strategy){
            case SINGLE:
                return new SingleLocationAlgorithm();
            case ABUNDANT:
                return new MostAbundantAlgorithm();
            case GREEDY:
                return new GreedyAlgorithm();
            default: return new SingleLocationAlgorithm();
        }
    }
}

